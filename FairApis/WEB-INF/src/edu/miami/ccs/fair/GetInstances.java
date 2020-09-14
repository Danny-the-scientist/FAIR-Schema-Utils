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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GetInstances extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		JSONArray ja = new JSONArray();
		JSONObject outobj = new JSONObject();
		FileReader inputfile=new FileReader("db_config.properties");  
	      
	    Properties props=new Properties();  
	    props.load(inputfile);  
	    
		String auth = "Authorization: " + " apiKey "+ props.getProperty("apikey");
	   
	    
		PutInstancesUtils iu = new PutInstancesUtils();
		String[] command;

		JSONArray schema;
		command = new String[] { "curl", "-X", "GET", "--header", "Accept: application/json", "--header",
				 auth, "https://resource.metadatacenter.org/folders/https%3A%2F%2Frepo.metadatacenter.org%2Ffolders%2Fbea02f48-8cf2-4f96-9efa-ce576915a20d/contents?resource_types=instance&version=all&publication_status=all&sort=-lastUpdatedOnTS&limit=1" };
		ProcessBuilder process = new ProcessBuilder(command).inheritIO();
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
			JSONArray res = (JSONArray) jb.get("resources");
			System.out.println(res);
			for(int j =0; j<res.size();j++){
				JSONObject indi_resource = (JSONObject) res.get(j);
				if(!indi_resource.get("lastUpdatedByUserName").toString().contentEquals("Amar Koleti")){
					if(iu.checkForInstance(indi_resource.get("@id").toString()) == false){
						
						iu.insertInstances(indi_resource.get("@id").toString(),getServletContext().getRealPath(""),indi_resource.get("lastUpdatedByUserName").toString());
						
					}
				}
			
			}
		
		} catch (FileNotFoundException e) {
			outobj.put("error", e.getMessage());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}