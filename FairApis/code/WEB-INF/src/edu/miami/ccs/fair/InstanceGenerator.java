package edu.miami.ccs.fair;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class InstanceGenerator {
	static String[] getTileGene(String fileName) throws IOException, SQLException, ParseException {
		
		return null;
	}
	static JSONArray instanceGen(String fileName,String category) throws IOException, SQLException, ParseException {
		
		JSONArray instances = new JSONArray();

		InstanceCreatorUtils icu = new InstanceCreatorUtils();
		JSONArray ja = icu.getCSVRecords(fileName);
		Map<String, List<String>> ontfields = icu.getOntologyProperties(category);
		Map<String, List<String>> cvfields = icu.getControlledVoc(category);
		ArrayList <String> regfields = icu.getRegularFiled(category);
		ArrayList <String> impfields = icu.getImportanceFields(category);
		
		PharosReady phar = new PharosReady();
		for (int j = 0; j < ja.size(); j++) {
			   
//				String pharos= phar.pharosReady(ja.get(j), category) ;
				
				JSONObject schema = new JSONObject();
				JSONObject obj = (JSONObject) ja.get(j);
				JSONObject context = new JSONObject();
				icu.properties(context, category);
				icu.setContext(context);
				schema.put("pharosReady", phar.pharosReady(ja.get(j), category));
				// JSONObject context = new JSONObject();
				schema.put("@context", context);
				Iterator<String> it = obj.keySet().iterator();
				icu.standardProperties(schema, category);
				while (it.hasNext()) {
					String key = it.next();
					if (ontfields.containsKey(key)) {

						String value = obj.get(key).toString();

						if (!value.equals("")) {
							if (ontfields.get(key).get(0).contains(";")) {
								icu.multiOntol(key, value, ontfields.get(key).get(0), schema);
							} else {
								icu.singleOntolvalue(key, value, ontfields.get(key).get(0), ontfields.get(key).get(1),
										schema);
							}
						} else {
							JSONObject val = new JSONObject();
							// val.put("@value", null);
							schema.put(key, val);
						}
					} else if (cvfields.containsKey(key)) {
						JSONObject vals = new JSONObject();
						String temp = obj.get(key).toString();
					
						if (!temp.isEmpty() && cvfields.get(key).contains(temp)) {
							vals.put("@value", temp);
						} else {
							vals.put("@value", null);
						}
						schema.put(key, vals);
					} else if(regfields.contains(key)) {
						JSONObject vals = new JSONObject();
				
						if (!obj.get(key).toString().isEmpty()) {
							vals.put("@value", obj.get(key).toString());
						} else {
							vals.put("@value", null);
						}

						schema.put(key, vals);
					}
					
				}
				

				instances.add(schema);
		}
		return instances;
	}

}
