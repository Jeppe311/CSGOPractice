package main;

import java.io.IOException;
import java.util.HashMap;

public class Main {

	public static void main(String[] args) {
		
		try {
			HashMap<String, Donator> donators = ExcelHandler.readDonators();
			System.out.print("Start top\t...\t");
			PictureCreator.createTopDonators(donators, 10);
			System.out.print("done.\n");
			System.out.print("Start rec\t...\t");
			PictureCreator.createRecentDonators(donators, 10);
			System.out.println("done.");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
