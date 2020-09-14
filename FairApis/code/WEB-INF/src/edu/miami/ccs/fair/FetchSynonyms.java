package edu.miami.ccs.fair;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FetchSynonyms extends HttpServlet {

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

		String id = request.getParameter("id");
		String file = request.getParameter("file");
		Iterable<CSVRecord> records;
		String SAMPLE_CSV_FILE_PATH = "/tmp/hsperfdata_akoleti/data/" + request.getParameter("file");
		Reader in = new FileReader(SAMPLE_CSV_FILE_PATH);
		records = CSVFormat.DEFAULT.withHeader().withSkipHeaderRecord(false).parse(in);
		Set<String> headers = records.iterator().next().toMap().keySet();
		DBConnection dbCon = new DBConnection(props.getProperty("JDBC_URL"), props.getProperty("USERNAME"),
				props.getProperty("PASSWORD"));
		Connection con = dbCon.getConnection();
		PreparedStatement stmt;
		String query = null;
		try {

			JSONArray jsArray = new JSONArray();
			JSONObject o = new JSONObject();
			for (String temp : headers) {
			
				query = "select distinct fieldname from category_descriptors where  metadatacategoryid = ? and fieldname = ?  ";
				stmt = con.prepareStatement(query);
				if (id != null) {
					stmt.setInt(1, Integer.parseInt(id));
					stmt.setString(2, temp);
				}

				try {
					ResultSet rs = stmt.executeQuery();

				
					if (rs.next()) {
						JSONArray js = new JSONArray();
						js.add(rs.getString("fieldname"));
						o.put(temp, js);
					} else {
						JSONArray jtemp = new JSONArray();
						o.put(temp, jtemp);
						

					}
				

				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			response.getWriter().print(o);
			response.flushBuffer();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}