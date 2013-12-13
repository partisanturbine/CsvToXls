package com.digsolab;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ConverterOptions {
    
	private String delimeter = null;
	private Format[] formats = null;
	private CommandLine line = null;
	
    private void parseConverterOptions() {
    	
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
}
