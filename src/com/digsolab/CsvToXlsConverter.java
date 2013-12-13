package com.digsolab;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Scanner;

public class CsvToXlsConverter {
	
	private ConverterOptions options = null;
    private Scanner csvScanner = null;
    
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
			openCSV(file);
			convertToXls();
		}
	}
	
	private void openCSV(File file) throws FileNotFoundException, IOException {
		FileInputStream fin = null;
		try {
		    fin = new FileInputStream(file);
		    csvScanner = new Scanner(fin);
		}
		finally {
			if (fin != null) {
			    fin.close();	
			}
		}
	}
	
	private void convertToXls() {
		String input = null;
		String[] csvCells = null;
		SXSSFWorkbook wb = new SXSSFWorkbook(100);
		while (csvScanner.hasNextLine()) {
			input = csvScanner.nextLine();
			csvCells = input.split(",");
			
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
