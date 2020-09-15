package edu.miami.ccs.dcic.standards;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Utils {
	
	static String date(){
		String pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());
		return date;
	}
	
	
	static JSONObject setSchema(){
		JSONObject schema = new JSONObject();
		schema.put("$schema", "http://json-schema.org/draft-04/schema#");
		
		return schema;
	}
	static JSONObject setType(String ty,  JSONObject valve){
//		JSONObject type = new JSONObject();
		valve.put("@type", ty);
		valve.put("type", "object");
		return valve;
	}
	
	static JSONObject setDescription(String des,  JSONObject valve){
//		JSONObject type = new JSONObject();
		valve.put("description", des);
		return valve;
	}
	
	static JSONObject setTitle(String title,  JSONObject valve){
//		JSONObject type = new JSONObject();
		valve.put("title", title);
		return valve;
	}
	
	
	static JSONObject setContext(JSONObject prop){
		JSONObject context = new JSONObject();
		JSONObject type = new JSONObject();
		JSONObject str = new JSONObject();
		JSONObject dt = new JSONObject();
		type.put("xsd", "http://www.w3.org/2001/XMLSchema#");
		type.put("pav", "http://purl.org/pav/");
		type.put("bibo", "http://purl.org/ontology/bibo/");
		type.put("oslc", "http://open-services.net/ns/core#");
		type.put("schema", "schema");
		str.put("@type", "xsd:string");
		type.put("schema:name", str);
		type.put("schema:description", str);
		dt.put("@type", "xsd:dateTime");
		type.put("pav:lastUpdatedOn", dt);
		type.put("pav:createdOn", dt);
		prop.put("@context", type);
		return prop;
		
	}
	
	static String[] splitString(String str) {
		String StringSplit[] = str.split("; ");
		return StringSplit;
		
	}
	
	static JSONObject objectProperties(String file, JSONObject prop) throws IOException {
		Reader reader = Files.newBufferedReader(Paths.get(file));

//		JSONObject prop = new JSONObject();

		
		for (CSVRecord csvRecord : CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {
			JSONArray propertylist = new JSONArray();
			JSONObject properies = new JSONObject();
			for (String temp: splitString(csvRecord.get("Schema/BioSchemas.org General Representation (closest possible)"))){
				propertylist.add(temp);
			}
			properies.put("enum",propertylist);
			prop.put(csvRecord.get("Property"),properies);
			
		}
		
		return prop;
		
	}
	
	public JSONObject requiredProperties( JSONObject obj) throws IOException {
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
	
	static JSONObject additionalProperties(JSONObject ad) {
		ad.put("additionalProperties", false);
		return ad;
	}
	
	static JSONObject valueOntologyConstraints(String ontology, String acronym, String branch, String uri, JSONObject valve) {
		
		JSONObject voc = new JSONObject();
		JSONObject v2 = new JSONObject();
		JSONArray vvcarray =  new JSONArray();
		
		String[] ontologies = splitString(acronym);
		String[] source = splitString(ontology);
		String[] uris = splitString(uri);
		String[] clsNames = splitString(branch);
		for (int i=0; i< ontologies.length; i++){
			voc.put("acronym",ontologies[i]);
			voc.put("source",source[i]);
			voc.put("uri",uris[i]);
			voc.put("name",clsNames[i]);
			voc.put("maxDepth",0);
		}
		vvcarray.add(voc);
		v2.put("branches",vvcarray);
		valve.put("_valueConstraints", v2);
		return valve;
		
	}
	
	static JSONObject valueVocabularyConstraints(String convoc, JSONObject valve) {
		
		String[] controlledVoc = splitString(convoc);

		JSONObject vvc = new JSONObject();
		JSONArray vvcarray =  new JSONArray();
		vvc.put("multipleChoice", true);
		vvc.put("requiredValue", false);
		JSONObject te = new JSONObject();
		for(String temp : controlledVoc) {
			te.put("label", temp);
		}
		vvcarray.add(te);
		vvc.put("literals",vvcarray);
		valve.put("_valueConstraints", vvc);

	
		return valve;
		
	}
	
	static JSONObject valueConstraints(JSONObject valve) {
		
		
		JSONObject val = new JSONObject();
		val.put("requiredValue",false);
		valve.put("_valueConstraints",val);
		return valve;
		
	}
	
	static JSONObject cardinality() {
		return null;
		
	}
	
	static JSONObject constantProperties(JSONObject prop) {
		JSONArray vvca =  new JSONArray();
		JSONObject p = new JSONObject();
		vvca.add("string");
		vvca.add("null");
		p.put("@value", vvca);
		prop.put("properties", p);
		return prop;	
	}
	
	
	static JSONObject individualProperties( String property,String desc,String ontology, String acronym, String branch, String uri,String properties, JSONObject prop) {
		
		JSONObject indp = new JSONObject();
		
		indp.put("schema:name",property);
		indp.put("schema:description",desc);
		
		indp.put("pav:createdOn",date());
		indp.put("schema:schemaVersion","1.5.0");
		
		indp.put("pav:lastUpdatedOn",date());
		indp.put("additionalProperties","false");
		indp.put("$schema","http://json-schema.org/draft-04/schema#");
		setContext(indp);
		constantProperties(indp);
		setType(properties,indp);
		setTitle(property,indp);
		setDescription(desc,indp);
		if(!ontology.isEmpty()){
		valueOntologyConstraints(ontology, acronym, branch,  uri, indp);
		}else if (!uri.isEmpty()){
			valueVocabularyConstraints(uri, indp);	
		}else{
			valueConstraints(indp);
		}
		setDescription(desc,indp);
		prop.put(property,indp);
		return prop;
		
	}
	
	
	
	
	
}
