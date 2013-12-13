package com.digsolab;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CsvToXlsConverter {
	
	private ConverterOptions options = null;
	
	public void convertToXls(String strSource, String strDestination)
			throws FileNotFoundException, IOException, IllegalArgumentException {
		File source = new File(strSource);
		File destination = new File(strDestination);
		if (!source.exists()) {
			
		}
	}
	
	public static void main(String[] args) {
		
	}
}
