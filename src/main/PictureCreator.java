package main;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

public class PictureCreator {

	private static BufferedImage base;
	private static String topFileName = "topgif.gif";
	private static String recFileName = "recgif.gif";

	public static void createRecentDonators(
			HashMap<String, Donator> allDonators, int nbrRecentDonators) {
		try {
			base = loadRecent();

			ArrayList<Donator> sortedDonators = getRecentDonators(allDonators,
					nbrRecentDonators);

			Font customFont = getFont();

			ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
			createGIF(sortedDonators, customFont, images, recFileName);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void createTopDonators(HashMap<String, Donator> allDonators,
			int nbrTopDonators) {
		try {
			base = loadTop();

			ArrayList<Donator> sortedDonators = getTopDonators(allDonators,
					nbrTopDonators);

			Font customFont = getFont();

			ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
			createGIF(sortedDonators, customFont, images, topFileName);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void createGIF(ArrayList<Donator> sortedDonators,
			Font customFont, ArrayList<BufferedImage> images,
			String outputFileName) throws FileNotFoundException, IOException,
			IIOException {
		for (Donator d : sortedDonators) {
			BufferedImage tempbase = deepCopy(base);
			for (BufferedImage img : d.createImages(tempbase, customFont)) {
				images.add(img);
			}
		}
		// create a new BufferedOutputStream with the last argument
		ImageOutputStream output;
		output = new FileImageOutputStream(new File(outputFileName));

		BufferedImage firstImage = images.get(0);

		// create a gif sequence with the type of the first image, 1 second
		// between frames, which loops continuously
		GifSequenceWriter writer = new GifSequenceWriter(output,
				firstImage.getType(), 1, true);

		// write out the first image to our sequence...
		writer.writeToSequence(firstImage);

		for (int i = 1; i < images.size(); i++) {
			BufferedImage nextImage = images.get(i);
			writer.writeToSequence(nextImage);
		}

		writer.close();
		output.close();
	}

	private static BufferedImage loadTop() {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("top.png"));
		} catch (IOException e) {
		}
		return img;
	}

	private static BufferedImage loadRecent() {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("recent.png"));
		} catch (IOException e) {
		}
		return img;
	}

	private static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	private static Font getFont() {
		Font customFont = null;
		try {
			customFont = Font.createFont(Font.TRUETYPE_FONT,
					new File("Cornerstone.ttf")).deriveFont(28f);
			GraphicsEnvironment ge = GraphicsEnvironment
					.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(
					"Cornerstone.ttf")));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}

		return customFont;
	}

	public static BufferedImage getBase() {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("base.jpg"));
		} catch (IOException e) {
		}
		return img;
	}

	private static ArrayList<Donator> getTopDonators(
			HashMap<String, Donator> allDonators, int nbrTopDonators) {
		ArrayList<Donator> sortList = new ArrayList<Donator>();
		for (String name : allDonators.keySet()) {
			sortList.add(allDonators.get(name));
		}
		Collections.sort(sortList);
		for (int i = 0; i < sortList.size(); i++) {
			sortList.get(i).setPlacement(i + 1);
		}
		ArrayList<Donator> topList = new ArrayList<Donator>();
		for (int i = 0; i < nbrTopDonators; i++) {
			topList.add(sortList.get(i));
		}
		return topList;
	}

	private static ArrayList<Donator> getRecentDonators(
			HashMap<String, Donator> allDonators, int nbrRecentDonators) {
		ArrayList<Donator> sortList = new ArrayList<Donator>();

		for (String name : allDonators.keySet()) {
			sortList.add(allDonators.get(name));
		}
		
		ArrayList<Donator> topList = new ArrayList<Donator>();
		for (int i = 0; i < nbrRecentDonators; i++) {
			Donator donor = sortList.get(0);
			for(Donator d : sortList){
				if(donor.getRowNbr() < d.getRowNbr()){
					donor = d;
				}
			}
			topList.add(donor);
			sortList.remove(donor);
		}
		return topList;
	}
}
