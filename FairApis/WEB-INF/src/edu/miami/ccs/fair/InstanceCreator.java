package edu.miami.ccs.fair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ProcessBuilder.Redirect;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class InstanceCreator extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		JSONArray ja = new JSONArray();
		 JSONObject outobj = new JSONObject();
		 boolean pharos = false;
		String category = request.getParameter("category");
		String user = request.getParameter("user");
		String resource = request.getParameter("resource");
		String file = request.getParameter("file");

		String[] command;
		
		FileReader inputfile=new FileReader("db_config.properties");  
	      
	    Properties props=new Properties();  
	    props.load(inputfile);  
	    
		String auth = "Authorization: " + " apiKey "+ props.getProperty("apikey");


		InstanceGenerator gen = new InstanceGenerator();
		
		PharosReady phar = new PharosReady();
		
		JSONArray schema;

		try {

			InstanceCreatorUtils icu = new InstanceCreatorUtils();
			// &category=2
			// gen.instanceGen("Cell_demo_final.csv", "2");
			schema = gen.instanceGen(file, category);
			
			List<String> ls = icu.getTitle(file);
			for (int i = 0; i < schema.size(); i++) {
				
//				JSONObject temp =(JSONObject) ((JSONObject)schema.get(i)).remove("pharosReady");
//				System.out.println((JSONObject)schema.get(i));
				String ins = schema.get(i).toString().replaceAll("\"pharosReady\":true,", "").replaceAll("\"pharosReady\":false,", "");
//				System.out.println(ins);
				String title = ls.get(i);

				System.out.println(ins);
			
				command = new String[] { "curl", "-X", "POST", "--header", "Content-Type: application/json", "--header",
						"Accept: application/json", "--header", auth, "-d", ins,
						"https://resource.metadatacenter.org/template-instances?folder_id=https%3A%2F%2Frepo.metadatacenter.org%2Ffolders%2Fbea02f48-8cf2-4f96-9efa-ce576915a20d" };
				ProcessBuilder process = new ProcessBuilder(command).inheritIO();
				try {
					String curlfile = null;
					curlfile = "curl" + System.currentTimeMillis() + ".json";
					process.redirectOutput(new File(getServletContext().getRealPath("") + curlfile));
					process.start();
					JSONParser jsonParser = new JSONParser();

					try {
						Thread.sleep(6000);
						try (FileReader reader = new FileReader(getServletContext().getRealPath("") + curlfile)) {
							// Read JSON file
							Object obj = jsonParser.parse(reader);
					

							JSONObject jb = (JSONObject) obj;
							
							pharos = phar.pharosReady(jb, category);
						
							Mongo mongo = new Mongo("dev3.ccs.miami.edu", 27017);
							DB db = mongo.getDB("dss");

							DBCollection collection = db.getCollection("rss");
							BasicDBObject document = new BasicDBObject();
							BasicDBObject removedocument = new BasicDBObject();
							removedocument.put("user", user);
							removedocument.put("resourceType", resource);
							removedocument.put("target", ((JSONObject) jb.get("Gene")).get("rdfs:label"));
							removedocument.put("name", title);
							
							// To do
							// add subtype for GE 
							document.put("user", user);
							document.put("resourceType", resource);
							document.put("id", jb.get("@id"));
							document.put("resource", jb);
							document.put("target", ((JSONObject) jb.get("Gene")).get("rdfs:label"));
							document.put("name", title);
							document.put("pharosReady", ((JSONObject) schema.get(i)).get("pharosReady").toString());
							System.out.println(document);
//							document.put("pharosReady", gen.);
							collection.remove(removedocument);
							collection.insert(document);
							outobj.put("resourceType", resource);
							outobj.put("id", jb.get("@id"));
							outobj.put("name", title);
							outobj.put("target", ((JSONObject) jb.get("Gene")).get("rdfs:label"));
							ja.add(outobj);
							response.flushBuffer();
							mongo.close();
						} catch (FileNotFoundException e) {
							outobj.put("error", e.getMessage());
							ja.add(outobj);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							outobj.put("error", e.getMessage());
							ja.add(outobj);
						}
						
					} catch (InterruptedException e) {
						outobj.put("error", e.getMessage());
						ja.add(outobj);
					}
				} catch (IOException e) {
					outobj.put("error", e.getMessage());
					ja.add(outobj);
				}
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			outobj.put("error", e1.getMessage());
			ja.add(outobj);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			outobj.put("error", e1.getMessage());
			ja.add(outobj);
		}

		response.getWriter().print(ja);
		response.flushBuffer();
	}

}