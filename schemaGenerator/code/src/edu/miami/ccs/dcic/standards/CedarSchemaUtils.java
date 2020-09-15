package edu.miami.ccs.dcic.standards;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.swing.text.html.parser.Entity;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.json.simple.JSONArray;

public class CedarSchemaUtils {
	
	
	
	public JSONObject pages() {
		JSONObject obj = new JSONObject();
		JSONArray list = new JSONArray();
		obj.put("pages",list);
		return obj;
		
	}
	
	public JSONObject propertyLabels(String file) throws IOException {
		JSONObject obj = new JSONObject();
		Reader reader = Files.newBufferedReader(Paths.get(file));
	
		JSONObject labels = new JSONObject();
		for (CSVRecord csvRecord : CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {
			labels.put(csvRecord.get("Property"), csvRecord.get("Property"));
		}
		
		obj.put("propertyLabels", labels);
		return obj;
		
	}
	
	public JSONObject propertyDescriptions(String file) throws IOException {
		JSONObject obj = new JSONObject();
		Reader reader = Files.newBufferedReader(Paths.get(file));
	
		JSONObject labels = new JSONObject();
		for (CSVRecord csvRecord : CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {
			labels.put(csvRecord.get("Property"), csvRecord.get("Description"));
		}
		
		obj.put("propertyDescriptions", labels);
		return obj;
		
	}
	
	public JSONObject order(String file) throws IOException {
		JSONObject obj = new JSONObject();
		Reader reader = Files.newBufferedReader(Paths.get(file));
	
		JSONArray list = new JSONArray();
		
		for (CSVRecord csvRecord : CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {
			list.add(csvRecord.get("Property"));
		}
		
		obj.put("order", list);
		return obj;
		
	}
	
	public JSONObject requiredProperties() throws IOException {
		JSONObject obj = new JSONObject();
		JSONArray list = new JSONArray();
		
		list.add("xsd");
		list.add("pav");
		list.add("schema");
		list.add("oslc");
		list.add("schema:isBasedOn");
		list.add("schema:name");
		list.add("schema:description");
		list.add("pav:createdOn");
		
		list.add("pav:createdBy");
		list.add("pav:lastUpdatedOn");
		list.add("oslc:modifiedBy");
		obj.put("required", list);
		return obj;
		
	
		
	}
	
	


}
