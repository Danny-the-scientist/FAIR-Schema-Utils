package edu.miami.ccs.fair;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class PharosReady {

	static boolean pharosReady(Object ob, String category) throws IOException, SQLException, ParseException {

		boolean pharos = false;
		InstanceCreatorUtils icu = new InstanceCreatorUtils();
		ArrayList<String> impfields = icu.getImportanceFields(category);
		
			JSONObject obj = (JSONObject) ob;
			Iterator<String> it = obj.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				if (impfields.contains(key)) {
					String value = obj.get(key).toString();
				  System.out.println(value.length());
					if (value.equals("") || value.equals(null)) {
						pharos = false;
						break;
					}else{
						pharos = true;
					}
				}
			}
		

		return pharos;
	}
	
	static boolean cedarPharosReady(Object ob, String category) throws IOException, SQLException, ParseException {

		boolean pharos = false;
		InstanceCreatorUtils icu = new InstanceCreatorUtils();
		ArrayList<String> impfields = icu.getImportanceFields(category);
		
			JSONObject obj = (JSONObject) ob;
			Iterator<String> it = obj.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				if (impfields.contains(key)) {
					JSONObject value = (JSONObject) obj.get(key);
					System.out.println(value.toString().length());
					if(value.toString().length()>2 ){
					
						if(value.containsKey("@value")){
							if(value.get("@value") != null){
								pharos = false;
								break;
							}else{
								pharos = true;
							}
						}else{
							pharos = true;
						}
						
						
					}else{
						System.out.println("#############");
						pharos = false;
						break;
					}
					
//					 System.out.println(value.toString().length());
//				     System.out.println(value.containsKey("@value"));
//					if (value.equals("") || value.equals(null)) {
//						pharos = false;
//						break;
//					}else{
//						pharos = true;
//					}
				}
			}
		

		return pharos;
	}
	
	
}
