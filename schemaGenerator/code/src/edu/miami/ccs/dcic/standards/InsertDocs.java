package edu.miami.ccs.dcic.standards;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class InsertDocs {
	
	static ServerAddress serverAddress = new ServerAddress("localhost", 27017);
	// final MongoCredential creds =
	// MongoCredential.createCredential("SigC","SignatureData","SigC".toCharArray());
	static MongoClient mongoClient = new MongoClient(serverAddress);
	static DB db = mongoClient.getDB("dss");
	static DBCollection coll = db.getCollection("rss");
	
	static void insertDocument(String name, String target, String type, String user, String file) throws FileNotFoundException, IOException, ParseException {
		BasicDBObject doc = new BasicDBObject();
		Object obj = new JSONParser().parse(new FileReader(file));
		JSONObject jo = (JSONObject) obj;
		doc.append("name",name);
		doc.append("target", target);
		doc.append("user", user);
		doc.append("resourceType", type);
		doc.append("id", jo.get("@id"));
		doc.append("resource", jo);
		try {
			System.out.println("insert a document");
			coll.insert(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
