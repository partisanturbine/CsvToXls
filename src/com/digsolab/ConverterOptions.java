package com.digsolab;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ConverterOptions {
    
	private static final char DELIMETER_KEY = 'd';
	private static final String COLUMNS_KEY = "columns";
	private static final String DEFAULT_DELIMETER = ",";
	private static final String COLUMNS_STRING_DELIMETER = ";";
	
	private String delimeter = null;
	private Format[] formats = null;
	private CommandLine line = null;
	
	private void parseDelimeter() {
		delimeter = line.getOptionValue(DELIMETER_KEY);
		if (delimeter == null) {
			delimeter = DEFAULT_DELIMETER;
		}
	}
	
	private Format createFormat(String[] formatOptions) {
		Format format = null;
		String formatKey = formatOptions[0];
		switch (formatKey) {
		    case "B": 
		    	format = new Format(Type.BOOLEAN, (formatOptions.length > 1) ? formatOptions[1] : null);
		    	break;
		    case "D": 
		    	format = new Format(Type.DATE, (formatOptions.length > 1) ? formatOptions[1] : null);
		    	break;
		    case "H":
		    	format = new Format(Type.HYPERLINK, (formatOptions.length > 1) ? formatOptions[1] : null);
		    	break;
		    case "N":
		    	format = new Format(Type.NUMBER, (formatOptions.length > 1) ? formatOptions[1] : null);
		    	break;
		    case "T":
		    	format = new Format(Type.TEXT, (formatOptions.length > 1) ? formatOptions[1] : null);
		    	break;
		    default:break;
	    }
		return format;
	}
	
	private void parseFormats() {
		String columnsParamsString = line.getOptionValue(COLUMNS_KEY);
		String[] columnParams = columnsParamsString.split(COLUMNS_STRING_DELIMETER);
		formats = new Format[columnParams.length];
		String currentFormat = null;
		for (int i = 0; i < columnParams.length; i++) {
			currentFormat = columnParams[i];
			String[] values = currentFormat.split(" ");
			Format format = createFormat(values);
			formats[i] = format;
		}
	}
	
    private void parseConverterOptions() {
    	parseDelimeter();
    	parseFormats();
    }
    
    private void getCLIArgs(String[] args) throws ParseException {
    	    Options options = new Options();
    		CommandLineParser parser = new BasicParser();
    		line = parser.parse(options, args);
    }
	
	public void parseOptions(String[] args) throws ParseException {
		getCLIArgs(args);
		parseConverterOptions();
	}
	
	public String getDelimeter() {
		return this.delimeter;
	}
	
	public Format[] getFormats() {
		return this.formats;
	}
}
