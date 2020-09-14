package edu.miami.ccs.fair;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PropertiesUtil {
	
	static JSONObject required(JSONObject obj) throws IOException {
		
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
	
	static JSONObject additional(JSONObject obj) throws IOException {
		obj.put("additionalProperties", false);
		return obj;
	}
	
	static JSONObject type(JSONObject obj) throws IOException {
		obj.put("type", "object");
		return obj;
	}
	
	static JSONObject staticTypes(JSONObject obj) throws IOException, ParseException {
		JSONParser parser = new JSONParser();
		obj.put("pav:lastUpdatedOn",parser.parse(" {\"type\": [\"string\",\"null\"],\"format\":\"date-time\"}"));
		obj.put("@type",parser.parse(" {\"oneOf\": [{\"format\":\"uri\",\"type\":\"string\"},{\"minItems\":1,\"uniqueItems\":true,\"type\":\"array\",\"items\":{\"format\":\"uri\",\"type\":\"string\"}}]}"));
		obj.put("schema:isBasedOn",parser.parse("{\"format\": \"uri\",\"type\": \"string\"}"));
		obj.put("@id",parser.parse("{\"format\": \"uri\",\"type\":[ \"string\",\"null\"]}"));
		obj.put("pav:createdOn",parser.parse(" {\"type\": [\"string\",\"null\"],\"format\":\"date-time\"}"));
		obj.put("pav:derivedFrom",parser.parse(" {\"type\": \"string\",\"format\":\"uri\"}"));
		obj.put("oslc:modifiedBy",parser.parse(" {\"type\": [\"string\",\"null\"],\"format\":\"uri\"}"));
		obj.put("schema:name",parser.parse(" {\"minLength\": 1,\"type\":\"string\"}"));


//	obj.put("rdfs", parser.parse("{\"type\": \"string\",\"format\": \"uri\",\"enum\": [\"http://www.w3.org/2000/01/rdf-schema#\"]},\"xsd\": {\"type\": \"string\",\"format\": \"uri\",\"enum\": [\"http://www.w3.org/2001/XMLSchema#\"]}"));
//	obj.put("pav",parser.parse("{\"type\": \"string\",\"format\": \"uri\",\"enum\": [\"http://purl.org/pav/\"]}"));
//	obj.put("schema",parser.parse(" {\"type\": \"string\",\"format\": \"uri\",\"enum\": [\"http://schema.org/\"]}"));
//	obj.put("oslc",parser.parse(" {\"type\": \"string\",\"format\": \"uri\",\"enum\": [\"http://open-services.net/ns/core#\"]}"));
//	obj.put("skos",parser.parse(" {\"type\": \"string\",\"format\": \"uri\",\"enum\": [\"http://www.w3.org/2004/02/skos/core#\"]}"));
//	obj.put("rdfs:label",parser.parse(" {\"type\": \"object\",\"properties\": {\"@type\": {\"type\": \"string\",\"enum\": [\"xsd:string\"]}}}"));
//	obj.put("schema:isBasedOn",parser.parse(" {\"type\": \"object\",\"properties\": {\"@type\": {\"type\": \"string\",\"enum\": [\"@id\"]}}}"));
//	obj.put("schema:name",parser.parse(" {\"type\": \"object\",\"properties\": {\"@type\": {\"type\": \"string\",\"enum\": [\"xsd:string\"]}}}"));
	obj.put("schema:description",parser.parse(" {\"type\": \"string\"}"));
//	obj.put("pav:createdOn",parser.parse(" {\"type\": \"object\",\"properties\": {\"@type\": {\"type\": \"string\",\"enum\": [\"xsd:dateTime\"]}}}"));
	obj.put("pav:createdBy",parser.parse(" {\"type\": [\"string\",\"null\"],\"format\":\"uri\"}"));
//	obj.put("pav:lastUpdatedOn",parser.parse(" {\"type\": \"object\",\"properties\": {\"@type\": {\"type\": \"string\",\"enum\": [\"xsd:dateTime\"]}}}"));
//	obj.put("oslc:modifiedBy",parser.parse(" {\"type\": \"object\",\"properties\": {\"@type\": {\"type\": \"string\",\"enum\": [\"@id\"]}}}"));
//	obj.put("skos:notation",parser.parse( "{\"type\": \"object\",\"properties\": {\"@type\": {\"type\": \"string\",\"enum\": [\"xsd:string\"]}}}"));
	return obj;
	}
	
	static JSONObject properties(ResultSet rs, JSONObject obj) throws IOException, SQLException, ParseException {
		JSONObject o = new JSONObject();
		JSONParser parser = new JSONParser();
		while(rs.next()){
			JSONObject ob = new JSONObject();
			JSONArray list = new JSONArray();
			if(rs.getString("fieldpropertyiri").contentEquals("") || rs.getString("fieldpropertyiri").isEmpty()){
				list.add("https://drughtargetontology.org/property/"+rs.getString("fieldname").replaceAll(" ", "_"));
			}else{
			list.add(rs.getString("fieldpropertyiri"));
			}
			ob.put("enum",list);
			o.put(rs.getString("fieldname"), ob);
		}
		o.put("pav:lastUpdatedOn",parser.parse(" {\"type\": \"object\",\"properties\": {\"@type\": {\"type\": \"string\",\"enum\": [\"xsd:dateTime\"]}}}"));
		o.put("skos",parser.parse(" {\"type\": \"string\",\"format\": \"uri\",\"enum\": [\"http://www.w3.org/2004/02/skos/core#\"]}"));
		o.put("rdfs", parser.parse("{\"type\": \"string\",\"format\": \"uri\",\"enum\": [\"http://www.w3.org/2000/01/rdf-schema#\"]}"));
		o.put("pav",parser.parse("{\"type\": \"string\",\"format\": \"uri\",\"enum\": [\"http://purl.org/pav/\"]}"));
		o.put("schema:isBasedOn",parser.parse(" {\"type\": \"object\",\"properties\": {\"@type\": {\"type\": \"string\",\"enum\": [\"@id\"]}}}"));
		o.put("rdfs:label",parser.parse(" {\"type\": \"object\",\"properties\": {\"@type\": {\"type\": \"string\",\"enum\": [\"xsd:string\"]}}}"));
		o.put("skos:notation",parser.parse( "{\"type\": \"object\",\"properties\": {\"@type\": {\"type\": \"string\",\"enum\": [\"xsd:string\"]}}}"));
		o.put("pav:createdOn",parser.parse(" {\"type\": \"object\",\"properties\": {\"@type\": {\"type\": \"string\",\"enum\": [\"xsd:dateTime\"]}}}"));
		o.put("schema",parser.parse(" {\"type\": \"string\",\"format\": \"uri\",\"enum\": [\"http://schema.org/\"]}"));
		o.put("oslc",parser.parse(" {\"type\": \"string\",\"format\": \"uri\",\"enum\": [\"http://open-services.net/ns/core#\"]}"));
		o.put("pav:derivedFrom",parser.parse("{ \"properties\": { \"@type\": { \"type\": \"string\", \"enum\": [\"@id\"]}},\"type\":\"object\"}"));
		o.put("oslc:modifiedBy",parser.parse(" {\"type\": \"object\",\"properties\": {\"@type\": {\"type\": \"string\",\"enum\": [\"@id\"]}},\"type\":\"object\"}"));
		o.put("schema:description",parser.parse(" {\"properties\": {\"@type\": {\"type\": \"string\",\"enum\": [\"xsd:string\"]}},\"type\":\"object\"}"));
		o.put("xsd",parser.parse("{\"type\": \"string\",\"format\": \"uri\",\"enum\": [\"http://www.w3.org/2001/XMLSchema#\"]}"));		
		o.put("pav:createdBy",parser.parse(" {\"type\": \"object\",\"properties\": {\"@type\": {\"type\": \"string\",\"enum\": [\"@id\"]}},\"type\":\"object\"}"));
		o.put("schema:name",parser.parse(" {\"type\": \"object\",\"properties\": {\"@type\": {\"type\": \"string\",\"enum\": [\"xsd:string\"]}},\"type\":\"object\"}"));

		obj.put("properties", o);
		return obj;
//		System.out.println();
	}
	



	
	
	
	
	
	
	
}
