package edu.miami.ccs.fair;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class Generator {
	static JSONObject Generator( String category,String type) throws IOException{
		FileReader reader=new FileReader("db_config.properties");  
	      
	    Properties props=new Properties();  
	    props.load(reader);  
	    
	    DBConnection dbCon = new DBConnection(props.getProperty("JDBC_URL"), props.getProperty("USERNAME"),
				props.getProperty("PASSWORD"));
		
//	DBConnection dbCon = new DBConnection("jdbc:postgresql://life.ccs.miami.edu:5432/FAIR_Standards", "sweng",
//			"sweng123");
	Connection con = dbCon.getConnection();
	PreparedStatement stmt;
	String query = null;
	String tempObject = null;
	JSONObject schema = new JSONObject();
	JSONObject indp = new JSONObject();
	JSONObject context =  new JSONObject();
	JSONObject misc =  new JSONObject();
	JSONObject pcontext =  new JSONObject();
	CedarSchemaUtils cs = new CedarSchemaUtils();
	Utils cu = new Utils();
	PropertiesUtil pu = new PropertiesUtil();
	schema.put("pav:version","0.0.1");
	schema.put("pav:lastUpdatedOn",cu.date());
	schema.put("schema:description","Cell Line Metadata Standards");
//	if(!type.contains("create")){
	if(type.toString().length() > 1)
	{
	schema.put("@id",type);
	}
	schema.put("pav:createdOn",cu.date());
	schema.put("@type","https://schema.metadatacenter.org/core/Template");
	schema.put("additionalProperties",false);
	try {

		JSONArray jsArray = new JSONArray();
		if (category != null) {
			query = "select cd.fieldname,mc.category, cd.fieldpropertyiri, cd.description,  cd.ontologyname, cd.ontologybranch, cd.ontologyiri, cd.controlledvocabulary, cd.fieldtype_cedar "
					+ " from public.category_descriptors cd "
					+ " inner join metadata_category mc on (mc.metadatacategoryid = cd.metadatacategoryid) "
					+ " where cd.metadatacategoryid = ? order by cd.categorydescriptorid ";
		}
		stmt = con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
		if (category != null) {
			stmt.setInt(1, Integer.parseInt(category));
		}
		try {
			ResultSet rs = stmt.executeQuery();

			cs.requiredProperties(schema);
			rs.beforeFirst();
			cs.propertyLabels(rs, misc);
			rs.beforeFirst();
			cs.propertyDescriptions(rs,misc);
			rs.beforeFirst();
			cs.pages(misc);
			rs.beforeFirst();
			cs.order(rs, misc);
			rs.beforeFirst();
			pu.staticTypes(indp);
			pu.properties(rs, pcontext);
			pu.additional(pcontext);
			pu.required(pcontext);
			pu.type(pcontext);
			indp.put("@context", pcontext);
			rs.beforeFirst();
			
				while(rs.next()){
				   
					String property = rs.getString("fieldname");
					String desc = rs.getString("description");
					String ontology  = rs.getString("ontologyname");
					String acronym  = rs.getString("ontologyname");
					String branch  = rs.getString("ontologybranch");
					String uri  = rs.getString("ontologyiri");
					String properties  = rs.getString("controlledvocabulary");
					String propertyiri  = rs.getString("fieldpropertyiri");
					String dtype  = rs.getString("fieldtype_cedar");
					schema.put("description",rs.getString("category")+" template schema generated by the CEDAR Template Editor 2.3.3");
					schema.put("title",rs.getString("category")+"  template schema");
					schema.put("schema:name",rs.getString("category"));
					cu.individualProperties(property,desc,ontology,acronym,branch,uri,properties,indp,propertyiri,dtype);
				
				}
				rs.beforeFirst();
				
				schema.put("properties",indp);
				schema.put("oslc:modifiedBy","https://metadatacenter.org/users/200114c6-0f5d-4d09-bd8c-d95ea607c545");
				schema.put("bibo:status","bibo:draft");
				schema.put("pav:createdBy","https://metadatacenter.org/users/200114c6-0f5d-4d09-bd8c-d95ea607c545");
				schema.put("schema:schemaVersion","1.5.0");
				schema.put("type","object");
				schema.put("@context",cs.setContext(context));
				schema.put("$schema","http://json-schema.org/draft-04/schema#");
				
				schema.put("_ui",misc);
			
				
			con.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return schema;
	}
}

