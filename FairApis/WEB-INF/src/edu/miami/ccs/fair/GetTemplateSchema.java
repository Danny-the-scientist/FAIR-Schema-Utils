package edu.miami.ccs.fair;

import java.io.BufferedReader;
import java.net.URLEncoder;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.io.UnsupportedEncodingException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import sun.misc.IOUtils;

public class GetTemplateSchema {

	static String GetJsonSchema(String fieldName) throws MalformedURLException, IOException, ParseException {
		JSONObject p = new JSONObject();

		String url = "https://resource.metadatacenter.org/templates/" + URLEncoder.encode(fieldName,StandardCharsets.UTF_8.toString()		);
	
		FileReader inputfile=new FileReader("db_config.properties");  
	      
	    Properties props=new Properties();  
	    props.load(inputfile);  
	    
		String auth = "Authorization: " + " apiKey "+ props.getProperty("apikey");
	
		String[] command = new String[] { "curl", "-X", "GET", "--header", "Accept: application/json", "--header",
				auth,
				url };

		ProcessBuilder pb = new ProcessBuilder(command);
		Process po;
		String result = "";

		try {

			po = pb.start();
			final BufferedReader reader = new BufferedReader(new InputStreamReader(po.getInputStream()));

			StringJoiner sj = new StringJoiner(System.getProperty("line.separator"));
			reader.lines().iterator().forEachRemaining(sj::add);
			result = sj.toString().replaceAll("'", "");

			// System.out.println(result);
			po.waitFor();
			po.destroy();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}
}
