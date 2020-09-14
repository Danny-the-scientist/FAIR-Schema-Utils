package edu.miami.ccs.fair;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class TemplateInstances extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("application/json");
	
		String category = request.getParameter("category");
		FileReader inputfile=new FileReader("db_config.properties");  
	      
	    Properties props=new Properties();  
	    props.load(inputfile);  
		
		DBConnection dbCon =  new DBConnection(props.getProperty("JDBC_URL"), props.getProperty("USERNAME"),
				props.getProperty("PASSWORD"));

		Connection con = dbCon.getConnection();
		PreparedStatement stmt;
		String query = null;
		try {
	
		
		JSONArray jsArray = new JSONArray(); 
		if(category==null){
		query = "select gt.id,gt.metadatacategoryid,mc.category,p.name,gt.templateid,gt.jsonschema   from public.generated_templates gt inner join metadata_category mc on (mc.metadatacategoryid = gt.metadatacategoryid)    inner join project p on (p.projectid = mc.projectid)";
		}else{
			query = "select gt.id,gt.metadatacategoryid,mc.category,p.name,gt.templateid,gt.jsonschema   from public.generated_templates gt inner join metadata_category mc on (mc.metadatacategoryid = gt.metadatacategoryid)    inner join project p on (p.projectid = mc.projectid) where mc.metadatacategoryid =  ? ";	
		}
		stmt = con.prepareStatement(query);
		if(category!=null){
		stmt.setInt(1, Integer.parseInt(category));
		}
		try {
			ResultSet rs = stmt.executeQuery();
			int columns = rs.getMetaData().getColumnCount();
	        while (rs.next()) {
	            JSONObject obj = new JSONObject();
	            for (int i = 1; i <= columns; i++) {
	                Object tempObject=rs.getObject(i);
	                obj.put(rs.getMetaData().getColumnLabel(i), tempObject);
	            }
	            jsArray.add(obj);
	        }
			response.getWriter().print(jsArray);
			response.flushBuffer();
			con.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}