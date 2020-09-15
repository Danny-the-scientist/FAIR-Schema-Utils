package edu.miami.ccs.dcic.standards;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CsvFileParser {
	 private static final String SAMPLE_CSV_FILE_PATH = "Small molecule_test120.csv";
		static String pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
		static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		static String date = simpleDateFormat.format(new Date());
		static String user = "amar";
	public static void main(String[] args) throws IOException{
		

				Iterable<CSVRecord> records;
				Reader in = new FileReader(SAMPLE_CSV_FILE_PATH);
				records = CSVFormat.DEFAULT.withHeader().withSkipHeaderRecord(false).parse(in);
				Set<String> headers = records.iterator().next().toMap().keySet();
				System.out.println(headers);
				if(headers.contains("Name")) {
				try (Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));

						
						Reader csvrecords = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));
						Reader reader3 = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));
				

						CSVParser totalRecords = new CSVParser(csvrecords,
								CSVFormat.DEFAULT.withHeader().withIgnoreHeaderCase().withSkipHeaderRecord());

				){
					int totalC = totalRecords.getRecords().size();
					System.out.println(totalC);
					
					for (CSVRecord csvRecord : CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader3)) 
					{
						System.out.print("insert into instance_provenance (name,resource_type,createdby,createdon,lastupdatedon,updatedby,updatedtm) values ( ");
						if(headers.contains("logP/hydrophobicity") || headers.contains("Water_solubility") || headers.contains("Molecular_weight") || headers.contains("ZINC_ID") ){
							System.out.println("'"+csvRecord.get("Name")+"',"+"'IDG Small Molecules','"+user+"','"+date+"'"+",'"+date+"','"+user+"','"+date+"')");
						}
					
						 
					}
				}
				}

	        
	    }

}
