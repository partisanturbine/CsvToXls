package com.digsolab;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;

public class CsvToXlsConverter {
	
	private ConverterOptions options = null;

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
