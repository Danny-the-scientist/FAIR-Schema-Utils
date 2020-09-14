package edu.miami.ccs.fair;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.swing.text.html.parser.Entity;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONArray;

public class CedarSchemaUtils {
	
	
	
	public JSONObject pages(JSONObject obj) {
		JSONArray list = new JSONArray();
		obj.put("pages",list);
		return obj;
		
	}
	
	public JSONObject propertyLabels(ResultSet rs,JSONObject obj) throws IOException, SQLException {
//		JSONObject obj = new JSONObject();
//		Reader reader = Files.newBufferedReader(Paths.get(file));
	
		JSONObject labels = new JSONObject();
		while(rs.next()){
			labels.put(rs.getString("fieldname"), rs.getString("fieldname"));

		}
		
		
		obj.put("propertyLabels", labels);
		return obj;
		
	}
	
	public JSONObject propertyDescriptions(ResultSet rs,JSONObject obj) throws IOException, SQLException {

		
	
		JSONObject labels = new JSONObject();
		while(rs.next()){
			labels.put(rs.getString("fieldname"), rs.getString("description"));	
		}
		
		
		obj.put("propertyDescriptions", labels);
		return obj;
		
	}
	
	static JSONObject setContext(JSONObject context){
		JSONObject type = new JSONObject();
		JSONObject str = new JSONObject();
		JSONObject dt = new JSONObject();
		JSONObject sk = new JSONObject();
		JSONObject na = new JSONObject();
		type.put("skos", "http://www.w3.org/2004/02/skos/core#");
		type.put("xsd", "http://www.w3.org/2001/XMLSchema#");
		type.put("pav", "http://purl.org/pav/");
		type.put("bibo", "http://purl.org/ontology/bibo/");
		type.put("oslc", "http://open-services.net/ns/core#");
		type.put("schema", "http://schema.org/");
		str.put("@type", "xsd:string");
		type.put("schema:name", str);
		type.put("schema:description", str);
		dt.put("@type", "xsd:dateTime");
		type.put("pav:lastUpdatedOn", dt);
		type.put("pav:createdOn", dt);
		na.put("@type", "@id");
		type.put("pav:createdBy", na);
//		sk.put("@type", "xsd:string");
//		type.put("skos:altLabel", sk);
//		type.put("skos:prefLabel", sk);
		type.put("oslc:modifiedBy", na);
		
		return type;
		
	}
	
	public JSONObject order(ResultSet rs,JSONObject obj) throws IOException, SQLException {
	
//		Reader reader = Files.newBufferedReader(Paths.get(file));
	
		JSONArray list = new JSONArray();
		while(rs.next()){
			list.add(rs.getString("fieldname").replaceAll(" ", "_"));	
		}
		
		
		
		obj.put("order", list);
		return obj;
		
	}
	
	public JSONObject requiredProperties(JSONObject obj) throws IOException {
	
		JSONArray list = new JSONArray();
		
		list.add("@context");
		list.add("@id");
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
