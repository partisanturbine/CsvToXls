package com.digsolab;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class ConverterTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	   ConverterOptions co = new ConverterOptions();
	   
       try {
 	   
           co.parseOptions(args);
           System.out.println("Delimeter: " + co.getDelimeter());
           System.out.println();
           System.out.println("Parameters");
           for (Format f : co.getFormats()) {
        	   System.out.println("Type : " + f.getType().toString() + " Mask: " + f.getMask());
           }
           System.out.println("\nSource " + co.getSource());
           System.out.println("Destination " + co.getDestination());
           CsvToXlsConverter converter = new CsvToXlsConverter(co);
           converter.convertToExcel();
       }
       catch (Throwable ex) {
    	   System.out.println(ex.toString());
       }
	}

}
