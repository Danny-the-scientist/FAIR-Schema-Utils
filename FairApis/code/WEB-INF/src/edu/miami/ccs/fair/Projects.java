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

public class Projects extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		DBConnection dbCon = new DBConnection("jdbc:postgresql://life.ccs.miami.edu:5432/FAIR_Standards", "sweng",
				"sweng123");
		Connection con = dbCon.getConnection();
		Statement stmt;
		try {
			stmt = con.createStatement();
		
		JSONArray jsArray = new JSONArray(); 
		String query = "select projectid,name,startdate,enddate,objectives from project ";
		try {
			ResultSet rs = stmt.executeQuery(query);
			int columns = rs.getMetaData().getColumnCount();
	        while (rs.next()) {
	            JSONObject obj = new JSONObject();
	            for (int i = 1; i <= columns; i++) {
	                Object tempObject=rs.getObject(i);
	                obj.put(rs.getMetaData().getColumnLabel(i), tempObject);
	            }
	            jsArray.add(obj);
	        }
//	        out.println(jsArray);

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
