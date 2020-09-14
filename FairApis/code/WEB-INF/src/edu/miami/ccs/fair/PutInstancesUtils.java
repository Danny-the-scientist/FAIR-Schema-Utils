package edu.miami.ccs.fair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class PutInstancesUtils {
	
	static ServerAddress serverAddress = new ServerAddress("dev3.ccs.miami.edu", 27017);
	static MongoClient mongoClient = new MongoClient(serverAddress);
	static DB db = mongoClient.getDB("dss");
	static DBCollection coll = db.getCollection("rss");
	static String auth = "Authorization: " + " apiKey d2e0213b9e43206deb2c54e08e54082a904ffb4edb40296cf6e110811d29ae9b";

	String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	
	static PharosReady pReady = new PharosReady();
	static Boolean checkForInstance(String id){
		Boolean notfound = false;
		
	
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("id", id);
		 DBCursor cursor = coll.find(whereQuery);
		 if(cursor.count()>0){
			 coll.remove(whereQuery);
			 notfound = false;
		 }	
		 
//		 mongoClient.close();
		return notfound;
		
	}
	
	static String getCategoryId(String category){
		String categoryId = "0";
		switch (category) {
		  case "Cell":
			  categoryId= "2";
		    break;
		  case "Antibody":
			  categoryId= "3";
		    break;
		  case "Genetic construct":
			  categoryId= "4";
		    break;
		  case "Small molecule":
			  categoryId= "5";
		    break;
		  case "Peptide":
			  categoryId= "6";
		    break;
		  case "Mouse":
			  categoryId= "8";
		    break;
		  case "Probe":
			  categoryId= "7";
			  break;
		  case "GPCR mouse imaging data":
			  categoryId= "9";
		    break;
		  case "NanoBRET data":
			  categoryId= "11";
		    break;
		  case "Proteomics data":
			  categoryId= "12";
		    break;
		  case "Channel activity data":
			  categoryId= "13";
			  break;
		  case "Mouse phenotype data":
			  categoryId= "14";
		    break;
		  case "Expression data":
			  categoryId= "15";
		    break;
		  case "Chemical tool":
			  categoryId= "16";
		    break;
		}
		return categoryId;
		
	}
	
	static void insertInstances(String id, String path, String user) throws IOException{
		

		String[] command = new String[] { "curl", "-X", "GET", "--header", "Accept: application/json", "--header",
				 auth, "https://resource.metadatacenter.org/template-instances/"+URLEncoder.encode(id, "UTF-8")+"?format=jsonld" };
		ProcessBuilder process = new ProcessBuilder(command).inheritIO();
		String curlfile = null;
		curlfile = "curl" + System.currentTimeMillis() + ".json";
		process.redirectOutput(new File(path+ curlfile));
		process.start();
		JSONParser jsonParser = new JSONParser();
		try {
			Thread.sleep(6000);
		try (FileReader reader = new FileReader(path + curlfile)) {
			// Read JSON file
			Object obj = jsonParser.parse(reader);
			BasicDBObject document = new BasicDBObject();
			document.put("user", user);
			
			document.put("resourceType", ((JSONObject) obj).get("schema:name").toString().replaceAll(" metadata", ""));
			document.put("pharosReady",pReady.cedarPharosReady(obj,getCategoryId(((JSONObject) obj).get("schema:name").toString().replaceAll(" metadata", ""))));
			document.put("id", ((JSONObject) obj).get("@id"));
			document.put("resource", obj);
			document.put("target", ((JSONObject) ((JSONObject) obj).get("Gene")).get("rdfs:label"));
			if(((JSONObject) obj).containsKey("Name")){
				document.put("name",  ((JSONObject) ((JSONObject) obj).get("Name")).get("@value"));	
			}else{
				document.put("name",  ((JSONObject) ((JSONObject) obj).get("Title")).get("@value"));
			}
		
			System.out.println(document);
			System.out.println("###############"+document.toString());
		
			coll.insert(document);
	
		
		} catch (FileNotFoundException e) {
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		
	}

	
}
