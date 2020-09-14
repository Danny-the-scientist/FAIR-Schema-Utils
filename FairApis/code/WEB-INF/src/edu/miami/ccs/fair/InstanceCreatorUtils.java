package edu.miami.ccs.fair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class InstanceCreatorUtils {
	
	static Properties props = new Properties();

	
	
	static List<String> getTitle(String fileName) throws IOException {
		
		String SAMPLE_CSV_FILE_PATH = "/tmp/hsperfdata_akoleti/data/" + fileName;
		Iterable<CSVRecord> records;
		Reader in = new FileReader(SAMPLE_CSV_FILE_PATH);
		Reader in2 = new FileReader(SAMPLE_CSV_FILE_PATH);
		records = CSVFormat.DEFAULT.withHeader().withSkipHeaderRecord(false).parse(in);
		Set<String> headers = records.iterator().next().toMap().keySet();
		Reader csvrecords = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));
		List<String> list = new ArrayList<>(); 
		for (CSVRecord csvRecord : CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in2)) {
			JSONObject obj = new JSONObject();
			if(headers.contains("Title")){
				 list.add(csvRecord.get("Title"));	
			}else if (headers.contains("Name")){
				 list.add(csvRecord.get("Name"));
			}
		}
	
		return list;
	
		
	}

	static JSONObject setContext(JSONObject type) {
		JSONObject context = new JSONObject();
		// JSONObject type = new JSONObject();
		JSONObject str = new JSONObject();
		JSONObject dt = new JSONObject();
		JSONObject sk = new JSONObject();
		JSONObject na = new JSONObject();
		type.put("skos", "http://www.w3.org/2004/02/skos/core#");
		type.put("xsd", "http://www.w3.org/2001/XMLSchema#");
		type.put("pav", "http://purl.org/pav/");
		type.put("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		type.put("oslc", "http://open-services.net/ns/core#");
		type.put("schema", "http://schema.org/");
		str.put("@type", "xsd:string");
		type.put("schema:name", str);
		type.put("rdfs:label", str);
		type.put("schema:description", str);
		dt.put("@type", "xsd:dateTime");
		type.put("pav:lastUpdatedOn", dt);
		type.put("pav:createdOn", dt);
		na.put("@type", "@id");
		type.put("pav:createdBy", na);
		type.put("schema:isBasedOn", na);
		type.put("pav:derivedFrom", na);
		sk.put("@type", "xsd:string");
		type.put("skos:notation", sk);
		type.put("oslc:modifiedBy", na);
		// prop.put("@context", type);
		return type;

	}

	static Map<String, String> getCustomFields(String category) throws SQLException {
		Map<String, List<String>> customfields = new HashMap<String, List<String>>();
		DBConnection dbCon = new DBConnection(props.getProperty("JDBC_URL"), props.getProperty("USERNAME"),
				props.getProperty("PASSWORD"));
		Connection con = dbCon.getConnection();
		PreparedStatement stmt;
		String query = null;
		query = "select fieldname,associated_milestone_link from category_descriptors where associated_milestone_link != '' and metadatacategoryid = ? ";
		stmt = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		stmt.setInt(1, Integer.parseInt(category));
		ResultSet rs = stmt.executeQuery();
		Map<String, String> customFields = new HashMap<String,String>();
		while(rs.next()){
			customFields.put(rs.getString(1),rs.getString(2));
		}
		
		return customFields;
	}
	static Map<String, List<String>> getOntologyProperties(String category) throws SQLException {
		Map<String, List<String>> ontofields = new HashMap<String, List<String>>();
		DBConnection dbCon = new DBConnection(props.getProperty("JDBC_URL"), props.getProperty("USERNAME"),
				props.getProperty("PASSWORD"));
		Connection con = dbCon.getConnection();
		PreparedStatement stmt;
		String query = null;
		query = "select fieldname,ontologyname,ontologyiri from category_descriptors c where ontologyname != '' and metadatacategoryid= ? ";
//		System.out.println(query);
		stmt = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		stmt.setInt(1, Integer.parseInt(category));
		
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			ontofields.put(rs.getString("fieldname"),
					Arrays.asList(rs.getString("ontologyname"), rs.getString("ontologyiri")));
		}
		con.close();
		return ontofields;
	}

	static Map<String, List<String>> getControlledVoc(String category) throws SQLException {
		Map<String, List<String>> contVocfields = new HashMap<String, List<String>>();
		DBConnection dbCon = new DBConnection(props.getProperty("JDBC_URL"), props.getProperty("USERNAME"),
				props.getProperty("PASSWORD"));
		Connection con = dbCon.getConnection();
		PreparedStatement stmt;
		String query = null;
		query = "select fieldname,controlledvocabulary from category_descriptors c where controlledvocabulary != '' and metadatacategoryid= ?";
		stmt = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		stmt.setInt(1, Integer.parseInt(category));
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			contVocfields.put(rs.getString("fieldname"),
					Arrays.asList(rs.getString("controlledvocabulary").toString().split(";")));
		}
		con.close();
		return contVocfields;

	}
	
	static ArrayList<String> getImportanceFields(String category) throws SQLException {
		
		ArrayList<String> impFields = new ArrayList<String>(50);
		DBConnection dbCon = new DBConnection(props.getProperty("JDBC_URL"), props.getProperty("USERNAME"),
				props.getProperty("PASSWORD"));
		Connection con = dbCon.getConnection();
		PreparedStatement stmt;
		String query = null;
		query = "select fieldname from category_descriptors where importance = 1  and metadatacategoryid= ?";
		stmt = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		stmt.setInt(1, Integer.parseInt(category));
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			impFields.add(rs.getString("fieldname"));
		}
		con.close();
		return impFields;

	}
	
	static ArrayList<String> getRegularFiled(String category) throws SQLException {
		
		ArrayList<String> regularFields = new ArrayList<String>(50);
		DBConnection dbCon = new DBConnection(props.getProperty("JDBC_URL"), props.getProperty("USERNAME"),
				props.getProperty("PASSWORD"));
		Connection con = dbCon.getConnection();
		PreparedStatement stmt;
		String query = null;
		query = "select fieldname from category_descriptors where ontologyname = '' and controlledvocabulary = '' and metadatacategoryid= ?";
		stmt = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		stmt.setInt(1, Integer.parseInt(category));
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			regularFields.add(rs.getString("fieldname"));
		}
		con.close();
		return regularFields;

	}

	static JSONObject standardProperties(JSONObject obj, String category) throws SQLException {
		DBConnection dbCon =   new DBConnection(props.getProperty("JDBC_URL"), props.getProperty("USERNAME"),
				props.getProperty("PASSWORD"));
		Connection con = dbCon.getConnection();
		PreparedStatement stmt;
		String query = null;
		query = "select m.category,m.uiname,g.templateid from generated_templates g inner join metadata_category m on (m.metadatacategoryid = g.metadatacategoryid) where m.metadatacategoryid = ?";
		stmt = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		stmt.setInt(1, Integer.parseInt(category));
		ResultSet rs = stmt.executeQuery();
		Utils u = new Utils();
		while (rs.next()) {
			obj.put("schema:isBasedOn", rs.getString("templateid"));
			obj.put("schema:name", rs.getString("category"));
			obj.put("schema:description", rs.getString("uiname")+" Metadata Standards");
			obj.put("pav:createdBy", "https://metadatacenter.org/users/200114c6-0f5d-4d09-bd8c-d95ea607c545");
			obj.put("oslc:modifiedBy", "https://metadatacenter.org/users/200114c6-0f5d-4d09-bd8c-d95ea607c545");
			obj.put("pav:createdOn",u.date());
			obj.put("pav:lastUpdatedOn",u.date());
		}
		con.close();
		return obj;

	}

	static JSONObject properties(JSONObject obj, String category) throws IOException, SQLException, ParseException {
		DBConnection dbCon =  new DBConnection(props.getProperty("JDBC_URL"), props.getProperty("USERNAME"),
				props.getProperty("PASSWORD"));
		Connection con = dbCon.getConnection();
		PreparedStatement stmt;
		String query = null;
		JSONObject o = new JSONObject();
		JSONParser parser = new JSONParser();
		query = "select * from category_descriptors c where metadatacategoryid= ? ";
		stmt = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		stmt.setInt(1, Integer.parseInt(category));

		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			JSONObject ob = new JSONObject();

			if (rs.getString("fieldpropertyiri").contentEquals("") || rs.getString("fieldpropertyiri").isEmpty()) {
				obj.put(rs.getString("fieldname"),
						"https://drughtargetontology.org/property/" + rs.getString("fieldname").replaceAll(" ", "_"));
			} else {
				obj.put(rs.getString("fieldname"), rs.getString("fieldpropertyiri"));
			}

		}
		con.close();
		return obj;
	}

	static JSONArray getCSVRecords(String fileName) throws IOException {
		String SAMPLE_CSV_FILE_PATH = "/tmp/hsperfdata_akoleti/data/" + fileName;
		Iterable<CSVRecord> records;
		Reader in = new FileReader(SAMPLE_CSV_FILE_PATH);
		Reader in2 = new FileReader(SAMPLE_CSV_FILE_PATH);
		records = CSVFormat.DEFAULT.withHeader().withSkipHeaderRecord(false).parse(in);
		Set<String> headers = records.iterator().next().toMap().keySet();
		Reader csvrecords = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));
		JSONArray jsArray = new JSONArray();
		for (CSVRecord csvRecord : CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in2)) {
			JSONObject obj = new JSONObject();
			for (String temp : headers) {
				obj.put(temp, csvRecord.get(temp));
			}
			jsArray.add(obj);
		}
		return jsArray;

	}

	static JSONObject multiOntol(String field, String values, String ontologys, JSONObject obj)
			throws IOException, ParseException {
		JSONObject jb = new JSONObject();
		JSONObject value = new JSONObject();
		JSONObject valempty = new JSONObject();
		valempty.put("@value", null);
		URL url;
		HttpURLConnection hcon;
		String urlString = "http://data.bioontology.org/search?q=" + values.replaceAll(" ", "%20") + "&ontologies="
				+ ontologys.replaceAll(";", ",") + "&roots_only=true&require_exact_match=true";
		url = new URL(urlString);
		hcon = (HttpURLConnection) url.openConnection();
		hcon.setRequestMethod("GET");
		hcon.setRequestProperty("Authorization", "apikey token=" + "5ee8b1b7-7853-468e-847c-714672f51c87");
		hcon.setRequestProperty("Accept", "application/json");
		BufferedReader rd;
		StringBuilder sb = new StringBuilder();
		String line;
		rd = new BufferedReader(new InputStreamReader(hcon.getInputStream()));
		JSONParser parser = new JSONParser();
		while ((line = rd.readLine()) != null) {
			JSONObject json = (JSONObject) parser.parse(line);
			if (json.get("collection").toString().length() > 2) {
				JSONArray arr = (JSONArray) json.get("collection");
				JSONObject colle = (JSONObject) arr.get(0);
				value.put("@id", colle.get("@id"));
				value.put("rdfs:label", colle.get("prefLabel"));
				obj.put(field, value);
			} else {
//				value.put("@value",  null);
				obj.put(field, value);
//				obj.put(field,valempty);
			}
		}
		return obj;

	}

	static JSONObject singleOntolvalue(String field, String values, String ontologys, String branch, JSONObject obj)
			throws IOException, ParseException {
		JSONObject jb = new JSONObject();
		JSONObject value = new JSONObject();
		URL url;
		HttpURLConnection hcon;
		String urlString = "http://data.bioontology.org/search?q=" + values.replaceAll(" ", "%20") + "&ontology="
				+ ontologys + "&subtree_root_id=" + URLEncoder.encode(branch, StandardCharsets.UTF_8.toString())
				+ "&require_exact_match=true";
		url = new URL(urlString);
		hcon = (HttpURLConnection) url.openConnection();
		hcon.setRequestMethod("GET");
		hcon.setRequestProperty("Authorization", "apikey token=" + "5ee8b1b7-7853-468e-847c-714672f51c87");
		hcon.setRequestProperty("Accept", "application/json");
		BufferedReader rd;
		StringBuilder sb = new StringBuilder();
		String line;
		rd = new BufferedReader(new InputStreamReader(hcon.getInputStream()));
		JSONParser parser = new JSONParser();
		while ((line = rd.readLine()) != null) {
			JSONObject json = (JSONObject) parser.parse(line);
			if (json.get("collection").toString().length() > 2) {
				JSONArray arr = (JSONArray) json.get("collection");
				JSONObject colle = (JSONObject) arr.get(0);
				value.put("@id", colle.get("@id"));
				value.put("rdfs:label", colle.get("prefLabel"));
//				value.put("@value",  colle.get("prefLabel"));
				obj.put(field, value);
			} else {
//				value.put("@value",  null);
				obj.put(field, value);
			}
		}

		return obj;

	}

}
