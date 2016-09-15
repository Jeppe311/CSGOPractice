package main;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelHandler {

	private static final String fileName = "donations.xls";

	public static HashMap<String, Donator> readDonators() throws IOException {
		
		HashMap<String, Donator> donators = new HashMap<String, Donator>();
		
		InputStream ExcelFileToRead = new FileInputStream(fileName);
		HSSFWorkbook wb = new HSSFWorkbook(ExcelFileToRead);

		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow row;
		HSSFCell cell;

		Iterator rows = sheet.rowIterator();

		int rowNbr = 0;
		while (rows.hasNext()) {

			rowNbr++;

			String name = null;
			String text = null;
			double amount = 0.0;

			row = (HSSFRow) rows.next();
			Iterator cells = row.cellIterator();

			while (cells.hasNext()) {
				cell = (HSSFCell) cells.next();

				if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
					if (cell.getColumnIndex() == 1) {
						name = cell.getStringCellValue();
					} else {
						text = cell.getStringCellValue();
						if(text.length()>64){
							text = text.substring(0,64);
						}
					}
				} else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
					// System.out.print(cell.getStringCellValue()+" ");
					if (cell.getColumnIndex() == 0) {
						amount = cell.getNumericCellValue();
					}
				} 
			}
			if(name != null){	
				if(donators.containsKey(name)){
					donators.get(name).addAmount(amount);
					donators.get(name).newRowNbr(rowNbr);
				} else {
					donators.put(name, new Donator(name, text, amount, rowNbr));
				}
			}
		}
		
		return donators;
	}

	public static void writeDonators() throws IOException {

		String excelFileName = "Test.xls";// name of excel file

		String sheetName = "Sheet1";// name of sheet

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);

		// iterating r number of rows
		for (int r = 0; r < 5; r++) {
			HSSFRow row = sheet.createRow(r);

			// iterating c number of columns
			for (int c = 0; c < 5; c++) {
				HSSFCell cell = row.createCell(c);

				cell.setCellValue("Cell " + r + " " + c);
			}
		}

		FileOutputStream fileOut = new FileOutputStream(excelFileName);

		// write this workbook to an Outputstream.
		wb.write(fileOut);
		fileOut.flush();
		fileOut.close();
	}

}