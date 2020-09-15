package edu.miami.ccs.dcic.standards;
import java.io.FileReader;

import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 

public class ReadJson {
	public static void main(String[] args) throws Exception  
    {
	Object obj = new JSONParser().parse(new FileReader("test.json"));
	JSONObject jo = (JSONObject) obj; 
	System.out.println(jo.get("@id"));
    }
}
