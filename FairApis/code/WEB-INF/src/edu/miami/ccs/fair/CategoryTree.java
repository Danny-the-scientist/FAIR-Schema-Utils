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
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CategoryTree extends HttpServlet {

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
		



		DBConnection dbCon = new DBConnection(props.getProperty("JDBC_URL"), props.getProperty("USERNAME"),
				props.getProperty("PASSWORD"));
		Connection con = dbCon.getConnection();
		PreparedStatement stmt;
		String query = null;
		try {

			JSONArray jsArray = new JSONArray();
				query = "select m.uiname,STRING_AGG(c.fieldname,'\",\"') as fields from project p  inner join metadata_category m on (m.projectid = p.projectid)"
						+ "     inner join category_descriptors c on (c.metadatacategoryid = m.metadatacategoryid) where p.projectid  =1 group by m.uiname ";
			stmt = con.prepareStatement(query);
			JSONObject o = new JSONObject();
			try {
				ResultSet rs = stmt.executeQuery();
			
				
				while (rs.next()) {
					JSONObject obj = new JSONObject();
					JSONArray js = new JSONArray();
					String temp = "[\""+rs.getString("fields")+"\"]";
					JSONParser parser = new JSONParser();
					try {
						js = (JSONArray) parser.parse(temp);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					o.put(rs.getString("uiname"), js);
//					jsArray.add(obj);
				}
//				o.put("data", jsArray);
				response.getWriter().print(o);
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