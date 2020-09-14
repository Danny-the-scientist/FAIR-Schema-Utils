package edu.miami.ccs.fair;

import java.io.FileInputStream;
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

public class MetadataCategories extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("application/json");
	
		Properties props = new Properties();
		
		InputStream is = null;
		try {
			is = getClass().getClassLoader().getResourceAsStream("db_config.properties");
		} catch (Exception e3) {
			e3.printStackTrace();
		}
    	try {
    		props.load(is);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
    	
		String project = request.getParameter("project");
		System.out.println();
		DBConnection dbCon = new DBConnection(props.getProperty("JDBC_URL"), props.getProperty("USERNAME"),
				props.getProperty("PASSWORD"));
		Connection con = dbCon.getConnection();
		PreparedStatement stmt;
		String query = null;
		try {
	
		
		JSONArray jsArray = new JSONArray(); 
		if(project==null){
		query = "select mc.metadatacategoryid,mc.category,mc.uiname,mc.projectid,mc.description,p.name  as projectname  from metadata_category mc  inner join project p on (p.projectid = mc.projectid)";
		}else{
			query = "select mc.metadatacategoryid,mc.category,mc.uiname,mc.projectid,mc.description,p.name   as projectname   from metadata_category mc  inner join project p on (p.projectid = mc.projectid) where p.projectid = ? ";	
		}
		stmt = con.prepareStatement(query);
		if(project!=null){
		stmt.setInt(1, Integer.parseInt(project));
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