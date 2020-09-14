package edu.miami.ccs.fair;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.net.*;
import java.io.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ValidationFieldCategories {

	static Properties props = new Properties();
	
	
	
	static DBConnection dbCon =  new DBConnection(props.getProperty("JDBC_URL"), props.getProperty("USERNAME"),
			props.getProperty("PASSWORD"));
	static Connection conn = dbCon.getConnection();

	static Map<String, String> customValidationFields(String id) throws SQLException {
		Statement stmt = null;
		String query = "select fieldname,associated_milestone_link from category_descriptors where associated_milestone_link != '' and metadatacategoryid =  "
				+ id;

		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);

		Map<String, String> customFields = new HashMap<String, String>();
		while (rs.next()) {
			customFields.put(rs.getString(1), rs.getString(2));
		}

		return customFields;

	}

	static JSONArray CustomValidation(String fieldName, String category, String value, String checklink, JSONArray ja,
			String custField) throws MalformedURLException, IOException, SQLException, ParseException {

		JSONObject jb = new JSONObject();
		if (value.toLowerCase().contentEquals("complete")) {
			System.out.println("i am here " + value);
			if (checklink.toString().length() > 5) {
			} else {
				jb.put(custField,
						"If the value is  complete for field " + fieldName + ", " + custField + " cannot be empty");
				ja.add(jb);
			}
		}
		return ja;
	}

	static JSONArray OntologyValidation(String fieldName, String value, String ontologys, String branch, List<String> l,
			JSONArray ja) throws MalformedURLException, IOException, SQLException, ParseException {
		// String field, String values, String ontologys, JSONObject obj
		JSONObject jb = new JSONObject();
		Statement stmt = null;
		URL url;
		HttpURLConnection hcon;
		boolean success = false;
		String result = "";

		String urlString = null;
		if (ontologys.contains(";")) {
			urlString = "http://data.bioontology.org/search?q=" + value.replaceAll(" ", "%20") + "&ontologies="
					+ ontologys.replaceAll(";", ",") + "&roots_only=true&require_exact_match=true";
			System.out.println(urlString);
		} else {
			urlString = "http://data.bioontology.org/search?q=" + value.replaceAll(" ", "%20") + "&ontology="
					+ ontologys + "&subtree_root_id=" + URLEncoder.encode(branch, StandardCharsets.UTF_8.toString())
					+ "&require_exact_match=true";
			System.out.println(urlString);
		}
		url = new URL(urlString);
		hcon = (HttpURLConnection) url.openConnection();
		hcon.setRequestMethod("GET");
		hcon.setRequestProperty("Authorization", "apikey token=" + props.getProperty("bioportalApiKey"));
		hcon.setRequestProperty("Accept", "application/json");
		BufferedReader rd;
		StringBuilder sb = new StringBuilder();
		String line;
		rd = new BufferedReader(new InputStreamReader(hcon.getInputStream()));
		JSONParser parser = new JSONParser();
		while ((line = rd.readLine()) != null) {
			JSONObject json = (JSONObject) parser.parse(line);
			if (json.get("collection").toString().length() > 2) {
				// jb.put("valid", "true");
				// success = true;
			} else {
				// jb.put("valid", "false");
				jb.put(fieldName, "values in the  " + fieldName + " field can only come from ontology " + ontologys);
				// success = false;
				ja.add(jb);
			}
		}

		return ja;
	}
}
