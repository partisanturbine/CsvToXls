package com.digsolab;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.io.ICsvListReader;
import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

public class CsvToXlsConverter {
	
	private static final String EXCEL_EXTENSION = ".xlsx";
	private static final String DATE_JAVA_PATTERN = "yyyy-mm-dd hh:mm:ss";
	private static final Logger log = Logger.getLogger(CsvToXlsConverter.class);
	private ConverterOptions options = null;
    private ICsvListReader listReader = null;
    private SXSSFWorkbook wb = null;
    private ArrayList<CellStyle> cellStyles = null;
    
    public CsvToXlsConverter(ConverterOptions options) {
    	this.options = options;
    }
    
    public void convertToExcel() throws FileNotFoundException,
                                                IOException, IllegalArgumentException {
    	String strSource = options.getSource();
    	String strDestination = options.getDestination();
    	convertToXls(strSource, strDestination);
    }
    
	private void convertToXls(String strSource, String strDestination)
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
		Properties log4jProperties = new Properties();
		log4jProperties.setProperty("log4j.rootLogger", "INFO, myConsoleAppender");
	    log4jProperties.setProperty("log4j.appender.myConsoleAppender", "org.apache.log4j.ConsoleAppender");
		log4jProperties.setProperty("log4j.appender.myConsoleAppender.layout", "org.apache.log4j.PatternLayout");
		log4jProperties.setProperty("log4j.appender.myConsoleAppender.layout.ConversionPattern", "%-5p %c %x - %m%n");
		PropertyConfigurator.configure(log4jProperties);
		for (File file : fileList) {	
			try {			
			    openCSV(file);
			    convertToXls();
			    saveXls(destination, file);
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
				if (listReader != null) {
				    listReader.close();
				}
			}
		}
	}
	
	private void openCSV(File file) throws FileNotFoundException, IOException {
			listReader = new CsvListReader(new FileReader(file), CsvPreference.STANDARD_PREFERENCE);
	}
	
	private void convertToXls() throws ParseException,
    NullPointerException, NumberFormatException, IOException {
		wb = new SXSSFWorkbook(100);
		Sheet sh = wb.createSheet();
		int rowIndex = 0;
		List<String> fieldsList = null;
		cellStyles = getCellStyles();
		//slistReader.read();
		try {
		while ((fieldsList = listReader.read()) != null) {
			if (rowIndex == 222022) {
			    log.info("!!!");
			}
			log.info("Reading " + rowIndex +"th row from csv");
			convertToXlsRow(sh, rowIndex++, fieldsList);
		}
		}
		catch (ParseException ex) {
			if (fieldsList != null) {
				System.out.println();
			}
		}
		catch (SuperCsvException e) {
			if (fieldsList != null) {
				System.out.println();
			}
		}
	}
	
    private void convertToXlsRow(Sheet sh, int rowIndex, List<String> csvRow) throws ParseException,
    NullPointerException, NumberFormatException {
		Row row = sh.createRow(rowIndex);
		int columnCount = this.options.getFormats().length;
		log.info("Creating " + rowIndex + "th row in SXSSFWorkbook");
		Cell cell = null;
		for (int cellnum = 0; cellnum < columnCount; cellnum++) {
			cell = row.createCell(cellnum);
			applyFormatting(cell, csvRow.get(cellnum), cellnum);
		}
	}
	
    private ArrayList<CellStyle> getCellStyles() {
    	ArrayList<CellStyle> styles = new ArrayList<>();
    	int columnCount = options.getFormats().length;
    	for (int i = 0; i < columnCount; i++) {
    		CellStyle cellStyle = wb.createCellStyle();
    		Format cellFormat = options.getFormats()[i];
        	String mask = cellFormat.getMask();
        	if (mask != null) {
        		cellStyle.setDataFormat(
            	        wb.getCreationHelper().createDataFormat().getFormat(mask));
        	}
    		styles.add(cellStyle);
    	}
    	return styles;  	
    }
    
    private void applyFormatting(Cell cell, String data, int cellIndex) throws ParseException,
    NullPointerException, NumberFormatException {
    	setCellValue(cell, data, cellIndex);
    	setCellStyle(cell, cellIndex);
    	log.info("Created cell number " + cellIndex);
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
    		Date dateValue = (new SimpleDateFormat(DATE_JAVA_PATTERN)).parse(data);
    		cell.setCellValue(dateValue);
    		break;
    	case HYPERLINK:
    		cell.setCellValue(data);
    		Hyperlink link = wb.getCreationHelper().createHyperlink(Hyperlink.LINK_URL);
    		cell.setHyperlink(link);
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
    	cell.setCellStyle(cellStyles.get(cellIndex));
    }
    
     
    private void saveXls(File destination, File file) throws FileNotFoundException, IOException {
    	String sourceFileName = file.getName();
    	String destinationFileName = sourceFileName.substring(0, sourceFileName.lastIndexOf('.')) + EXCEL_EXTENSION;
    	FileOutputStream fout = null;
    	try {
    	    fout = new FileOutputStream(new File(destination, destinationFileName));
    	    log.info("Saving Excel file");
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
	
	public ICsvListReader getReader() {
		return listReader;
	}
	
	public SXSSFWorkbook getWB() {
		return wb;
	}
}
