package edu.miami.ccs.fair;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
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

public class DownloadStandards extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/octet-stream");

		String category = request.getParameter("category");

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
		
		DBConnection dbCon = new DBConnection(props.getProperty("JDBC_URL"), props.getProperty("USERNAME"),
				props.getProperty("PASSWORD"));
		Connection con = dbCon.getConnection();
		PreparedStatement stmt;
		String query = null;
		String tempObject = null;
		try {

			JSONArray jsArray = new JSONArray();
			if (category != null) {
				query = "select cd.fieldname,mc.category, cd.fieldpropertyiri, cd.description, cd.importance, cd.ontologyname, cd.ontologybranch, cd.ontologyiri, cd.controlledvocabulary, cd.fieldcardinality "
						+ " from public.category_descriptors cd "
						+ " inner join metadata_category mc on (mc.metadatacategoryid = cd.metadatacategoryid) "
						+ " where cd.metadatacategoryid = ? order by cd.categorydescriptorid ";
			}
			stmt = con.prepareStatement(query);
			if (category != null) {
				stmt.setInt(1, Integer.parseInt(category));
			}
			try {
				ResultSet rs = stmt.executeQuery();
//				List<String[]> list = new List<String[]>();
				int columns = rs.getMetaData().getColumnCount();
					while(rs.next()){
					    response.setHeader( "Content-Disposition",
				   		         String.format("attachment; filename="+rs.getString("category")+".csv"));
					    ArrayList<String> data =new ArrayList <>();
					    for (int i = 1; i <= columns; i++) {
			              
			                data.add(i, rs.getString(i));
			            }
					  
						response.getWriter().print(data);
			    		response.flushBuffer();
					}
					
				
				
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