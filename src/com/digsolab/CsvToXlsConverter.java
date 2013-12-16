package com.digsolab;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class CsvToXlsConverter {
	
	private static final String EXCEL_EXTENSION = ".xls";
	private static final String DATE_PATTERN = "";
	private ConverterOptions options = null;
    private Scanner csvScanner = null;
    private SXSSFWorkbook wb = null;
    
	public void convertToXls(String strSource, String strDestination)
			throws FileNotFoundException, IOException, IllegalArgumentException {
		File source = new File(strSource);
		File destination = new File(strDestination);
		if (!source.exists()) {
			throw new IllegalArgumentException("The source for the "
					+ ".csv file(s) cannot be found");
		}
		if (!destination.exists()) {
			throw new IllegalArgumentException("The folder for converted excel "
		            + "cannot be found");
		}
		if (!destination.isDirectory()) {
			throw new IllegalArgumentException("The destination path"
					+ "is not a directory");
		}
		File[] fileList = null;
		if (source.isDirectory()) {
			fileList = source.listFiles(new CSVFilenameFilter());
		} else {
			fileList = new File[] {source};
		}
		for (File file : fileList) {	
			try {
			    openCSV(file);
			    convertToXls();
			    saveXls(file);
			}
			catch (ParseException pex) {
				//TODO: logging
			}
			catch (NullPointerException nex) {
				//TODO: logging
			}
			catch (NumberFormatException numEx) {
				//TODO: logging
			}
			finally { 
				if (wb != null) {
				    wb.dispose();
				}
				if (csvScanner != null) {
				    csvScanner.close();
				}
			}
		}
	}
	
	private void openCSV(File file) throws FileNotFoundException, IOException {
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(file);
			csvScanner = new Scanner(fin);
		}
		catch (FileNotFoundException fEx) {
			if (fin != null) {
			    fin.close();
			}
			throw new FileNotFoundException();
		}
		catch (IOException ioEx) {
			if (fin != null) {
			    fin.close();
			}
			throw new IOException();
		}
	}
	
	
	private void convertToXls() throws ParseException,
    NullPointerException, NumberFormatException {
		String input = null;
		String[] csvCells = null;
		wb = new SXSSFWorkbook(100);
		Sheet sh = wb.createSheet();
		int rowIndex = 0;
		while (csvScanner.hasNextLine()) {
			input = csvScanner.nextLine();
			csvCells = input.split(options.getDelimeter());
			convertToXlsRow(sh, rowIndex++, csvCells);
		}
	}
	
    private void convertToXlsRow(Sheet sh, int rowIndex, String[] csvRow) throws ParseException,
    NullPointerException, NumberFormatException {
		Row row = sh.createRow(rowIndex);
		int cellTotal = csvRow.length;
		for (int cellnum = 0; cellnum < cellTotal; cellnum++) {
			Cell cell = row.createCell(cellnum);
			applyFormatting(cell, csvRow[cellnum], cellnum);
		}
	}
	
    private void setCellValue(Cell cell, String data, int cellIndex) throws ParseException,
                   NullPointerException, NumberFormatException {
    	Format cellFormat = options.getFormats()[cellIndex];
    	Type cellType = cellFormat.getType();
    	switch (cellType) {
    	case BOOLEAN:
    		 boolean boolValue = Boolean.parseBoolean(data);
    		 cell.setCellValue(boolValue);
    		 break;
    	case DATE:
    		Date dateValue = (new SimpleDateFormat()).parse(data);
    		cell.setCellValue(dateValue);
    		break;
    	case HYPERLINK:
    		cell.setCellValue(data);
    		Hyperlink link = wb.getCreationHelper().createHyperlink(Hyperlink.LINK_URL);
    		link.setAddress(data);
    		break;
    	case NUMBER:
    		double numValue = Double.parseDouble(data);
    		cell.setCellValue(numValue);
    		break;
    	case TEXT:
    		cell.setCellValue(data);
    		break;
    	default: break;
    	}
    }
    
    private void setCellStyle(Cell cell, int cellIndex) {
    	CellStyle cellStyle = wb.createCellStyle();
    	Format cellFormat = options.getFormats()[cellIndex];
    	String mask = cellFormat.getMask();
    	cellStyle.setDataFormat(
    	        wb.getCreationHelper().createDataFormat().getFormat(mask));
    }
    
    private void applyFormatting(Cell cell, String data, int cellIndex) throws ParseException,
    NullPointerException, NumberFormatException {
    	setCellValue(cell, data, cellIndex);
    	setCellStyle(cell, cellIndex);
    }
    
    private void saveXls(File file) throws FileNotFoundException, IOException {
    	String sourceFileName = file.getName();
    	String destinationFileName = sourceFileName.substring(0, sourceFileName.lastIndexOf('.')) + EXCEL_EXTENSION;
    	FileOutputStream fout = null;
    	try {
    	    fout = new FileOutputStream(destinationFileName);
    	    wb.write(fout);
    	}
    	finally {
    		if (fout != null) {
    			fout.close();
    		}
    	}
    }
    
	private class CSVFilenameFilter implements FilenameFilter {

		@Override
		public boolean accept(File dir, String name) {		
			return name.endsWith(".csv");
		}
		
	}
	
	public static void main(String[] args) {
		
	}
}
