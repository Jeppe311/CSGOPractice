package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

public class Donator implements Comparable<Donator> {
	
	private static Color red = new Color(245, 73, 77);
	private static Color white = new Color(241, 241, 241);
	
	private String name;
	private String latestMessage;
	private double amount;
	private int rowNbr;
	private int placement;

	public Donator(String name, String message, double amount, int rowNbr) {
		this.name = name;
		this.amount = amount;
		this.latestMessage = message;
		this.rowNbr = rowNbr;
		this.placement = -1;
	}

	public void setPlacement(int place) {
		placement = place;
	}

	public ArrayList<BufferedImage> createImages(BufferedImage image,
			Font customFont) {
		ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
		Graphics graphics = image.getGraphics();
		graphics.setFont(customFont);
		graphics.setColor(red);
		FontMetrics metrics = graphics.getFontMetrics(customFont);
		drawTopInfo(graphics, metrics);
		images.add(image);
		drawText(image, metrics, images, customFont);

		return images;
	}



	private void drawTopInfo(Graphics graphics, FontMetrics metrics) {
		String place = "" + placement;
		double placeEnd = metrics.getStringBounds(place, graphics).getWidth() + 215;
		graphics.drawString(place, 215, 36);

		String amountText = "" + amount;
		if (amountText.split("\\.")[1].length() < 2) {
			amountText += "0";
		}
		double amountStart = 555 - metrics
				.getStringBounds(amountText, graphics).getWidth();
		graphics.drawString(amountText, ((int) (amountStart)), 36);

		double allowedNameWidth = amountStart - placeEnd - 50;
		double nameWidth = metrics.getStringBounds(name, graphics).getWidth();
		while (nameWidth > allowedNameWidth) {
			name = name.substring(0, name.length() - 1);
			nameWidth = metrics.getStringBounds(name, graphics).getWidth();
		}
		double margin = (allowedNameWidth - nameWidth);
		graphics.drawString(name, (int) (placeEnd + margin / 2) + 20, 36);

	}
	
	private void drawText(BufferedImage image, FontMetrics metrics,
			ArrayList<BufferedImage> images, Font customFont) {
		if (latestMessage == null) {
			latestMessage = "no message";
		}
		BufferedImage empty = deepCopy(image);
		for (int i = 0; i < 3; i++) {
			if (i % 2 == 0) {
				image = deepCopy(empty);
				Graphics graphics = image.getGraphics();
				graphics.setFont(customFont);
				graphics.setColor(red);
				graphics.drawString(".", 20, 90);
				images.add(image);
			} else {
				images.add(empty);
			}
		}

		String currentString = "";
		for (int i = 0; i < latestMessage.length(); i++) {
			currentString = latestMessage.substring(0, i + 1);
			while (currentString.charAt(currentString.length() - 1) == ' ') {
				i++;
				if (i == latestMessage.length())
					break;
				currentString = latestMessage.substring(0, i + 1);
			}
			
			while(metrics.getStringBounds(currentString, image.getGraphics()).getWidth() > 535){
				currentString = currentString.substring(1, currentString.length());
			}
			
			image = deepCopy(empty);
			Graphics graphics = image.getGraphics();
			graphics.setFont(customFont);
			graphics.setColor(red);
			graphics.drawString(currentString, 20, 90);
			images.add(image);
		}

		for (int i = 0; i < 5; i++) {
			if (i % 2 == 0) {
				image = deepCopy(empty);
				Graphics graphics = image.getGraphics();
				graphics.setFont(customFont);
				graphics.setColor(red);
				graphics.drawString(currentString + ".", 20, 90);
				images.add(image);
			} else {
				image = deepCopy(empty);
				Graphics graphics = image.getGraphics();
				graphics.setFont(customFont);
				graphics.setColor(red);
				graphics.drawString(currentString, 20, 90);
				images.add(image);
			}
		}
	}

	private static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	@Override
	public String toString() {
		return "Donator [name=" + name + ", latestMessage=" + latestMessage
				+ ", donation=" + amount + ", rowNbr=" + rowNbr + "]\n";
	}

	public String getName() {
		return name;
	}

	public String getLatestMessage() {
		return latestMessage;
	}

	public Double getDonation() {
		return amount;
	}
	
	public int getRowNbr(){
		return rowNbr;
	}

	public int compareTo(Donator other) {
		if (amount < other.amount) {
			return 1;
		} else if (amount == other.amount) {
			return 0;
		} else {
			return -1;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Donator other = (Donator) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public void addAmount(double amount) {
		this.amount += amount;
	}

	public void newRowNbr(int rowNbr) {
		this.rowNbr = rowNbr;
	}
}
