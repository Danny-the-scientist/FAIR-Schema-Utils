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

public class Utils {
	
	static String date(){
	
		String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
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
		valve.put("@type", "https://schema.metadatacenter.org/core/TemplateField");
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
		sk.put("@type", "xsd:string");
		type.put("skos:altLabel", sk);
		type.put("skos:prefLabel", sk);
		type.put("oslc:modifiedBy", na);
		prop.put("@context", type);
		return prop;
		
	}
	
	static String[] splitString(String str) {
		String StringSplit[] = str.split(";");
		return StringSplit;
		
	}
	
//	static JSONObject objectProperties(String file, JSONObject prop) throws IOException {
//		Reader reader = Files.newBufferedReader(Paths.get(file));
//
////		JSONObject prop = new JSONObject();
//
//		
//		for (CSVRecord csvRecord : CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {
//			JSONArray propertylist = new JSONArray();
//			JSONObject properies = new JSONObject();
//			for (String temp: splitString(csvRecord.get("Schema/BioSchemas.org General Representation (closest possible)"))){
//				propertylist.add(temp);
//			}
//			properies.put("enum",propertylist);
//			prop.put(csvRecord.get("Property"),properies);
//			
//		}
//		
//		return prop;
//		
//	}
	
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
		
		System.out.println(ontology);
		
		
		JSONObject v2 = new JSONObject();
		JSONArray vvcarray =  new JSONArray();
		JSONArray tempempty =  new JSONArray();
		
		String[] ontologies = splitString(acronym);
		String[] source = splitString(ontology);
		String[] uris = splitString(uri);
		String[] clsNames = splitString(branch);
		for (int i=0; i< ontologies.length; i++){
			JSONObject voc = new JSONObject();
			voc.put("acronym",ontologies[i]);
			voc.put("source",source[i]);
			voc.put("uri",uris[i]);
			voc.put("name",clsNames[i]);
			voc.put("maxDepth",0);
			vvcarray.add(voc);
		}
		
		
	
		v2.put("ontologies",tempempty);
		v2.put("valueSets",tempempty);
		v2.put("classes",tempempty);
		v2.put("branches",vvcarray);
		v2.put("requiredValue", false);
		v2.put("multipleChoice", false);
		valve.remove("required");
		valve.put("_valueConstraints", v2);
		
//		System.out.println(valve);
		
		return valve;
		
	}
	
	static JSONObject valueVocabularyConstraints(String convoc, JSONObject valve) {
		

		
		String[] controlledVoc = splitString(convoc);
		JSONObject vvc = new JSONObject();
		JSONArray vvcarray =  new JSONArray();
		
		vvc.put("multipleChoice", false);
		vvc.put("requiredValue", false);
		
		for(int k=0; k< controlledVoc.length; k++) {
			JSONObject t = new JSONObject();
			t.put("label", controlledVoc[k]);
			vvcarray.add(t);
		}
		
		vvc.put("literals",vvcarray);
		valve.put("_valueConstraints", vvc);
		
		return valve;
		
////		String[] controlledVoc = splitString(convoc);
//
//		JSONObject vvc = new JSONObject();
//		JSONArray vvcarray =  new JSONArray();
//		vvc.put("multipleChoice", false);
//		vvc.put("requiredValue", false);
//		JSONObject te = new JSONObject();
//			te.put("label", "Status 1");
//			te.put("label", "Status 2");
//		vvc.put("literals",te);
//		valve.put("_valueConstraints", vvc);
//
//	
//		return valve;
		
	}
	
	static JSONObject valueConstraints(JSONObject valve) {
		
		
//		System.out.println(( (JSONObject) valve.get("_ui")).get("inputType"));
		
		JSONObject numtype = new JSONObject();
		numtype.put("type", "string");
		numtype.put("format", "uri");
		
		JSONArray res = new JSONArray();
		res.add("@value");
		res.add("@type");
		
		JSONObject val = new JSONObject();
		JSONObject lit = new JSONObject();
		if(( (JSONObject) valve.get("_ui")).get("inputType").toString().contains("numeric")){
				val.put("numberType", "xsd:decimal");
				val.put("requiredValue",false);
//				.put("@type", numtype)
//				valve = (JSONObject) ((JSONObject) ((JSONObject) valve.get("properties")).remove("@type"));
//				JSONObject v1= (JSONObject) ((JSONObject) v.get("properties")).put("@type", numtype);
				valve.put("_valueConstraints",val);		
				valve.put("required",res);
		}else{
		val.put("requiredValue",false);
		val.put("multipleChoice",false);
		valve.put("_valueConstraints",val);
		}
		
		System.out.println(valve);
		return valve;
		
	}
	
	static JSONObject cardinality() {
		return null;
		
	}
	
	static JSONObject constantProperties(JSONObject prop, String ontology) throws ParseException {
		JSONArray vvca =  new JSONArray();
		JSONObject p = new JSONObject();
		JSONParser parser = new JSONParser();
//		vvca.add("string");
//		vvca.add("null");
	
		if(!ontology.isEmpty() || !ontology.contentEquals("") ){
			p.put("@id", parser.parse("{ \"format\": \"uri\",\"type\": \"string\"}"));
		}else{
		p.put("@value", parser.parse("{ \"type\": [\"string\",\"null\"]}"));
		}
		p.put("@type", parser.parse("{ \"oneOf\": [{\"format\": \"uri\",\"type\": \"string\"},{ \"minItems\": 1,\"items\": {\"format\": \"uri\",\"type\": \"string\"},\"uniqueItems\": true,\"type\": \"array\"}]}"));
		p.put("rdfs:label",parser.parse("{ \"type\": [\"string\",\"null\"]}"));
		prop.put("properties", p);
		return prop;	
	}
	
	
	static JSONObject individualProperties( String property,String desc,String ontology, String acronym, String branch, String uri,String properties, JSONObject prop,String propertiesiri,String dtype  ) throws ParseException {
		
		JSONObject indp = new JSONObject();
		JSONParser parser = new JSONParser();
		indp.put("schema:name",property);
		indp.put("schema:description",desc);
		
		indp.put("pav:createdOn",date());
		indp.put("schema:schemaVersion","1.5.0");
		
		indp.put("pav:lastUpdatedOn",date());
		indp.put("additionalProperties",false);
		indp.put("$schema","http://json-schema.org/draft-04/schema#");
		indp.put("oslc:modifiedBy","https://metadatacenter.org/users/200114c6-0f5d-4d09-bd8c-d95ea607c545");
		indp.put("pav:createdBy","https://metadatacenter.org/users/200114c6-0f5d-4d09-bd8c-d95ea607c545");
		indp.put("schema:schemaVersion", "1.5.0");
//		if(propertiesiri.contentEquals("") || propertiesiri.isEmpty()){
			indp.put("@id","temp_"+System.currentTimeMillis()+property.replaceAll(" ", "_"));
//		}else{
//			indp.put("@id",propertiesiri);	
//		}
	
		indp.put("additionalProperties", false);
		if(!ontology.isEmpty() || !ontology.contentEquals("")){
		indp.put("required",parser.parse(" [\"@value\"]"));
		}

		JSONObject inputtype = new JSONObject();
		inputtype.put("inputType", dtype);
		indp.put("_ui", inputtype);
		setContext(indp);
		constantProperties(indp,ontology);
		setType(properties,indp);
		setTitle(property,indp);
		setDescription(desc,indp);
		if(!ontology.isEmpty()){
		valueOntologyConstraints(ontology, acronym, branch,  uri, indp);
		}else if (!properties.isEmpty()){
			valueVocabularyConstraints(properties, indp);	
		}else{
			valueConstraints(indp);
		}
		prop.put(property,indp);
		return prop;
		
	}
	
	
	
	
	
}
