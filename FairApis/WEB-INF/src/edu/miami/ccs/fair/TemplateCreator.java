package edu.miami.ccs.fair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ProcessBuilder.Redirect;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TemplateCreator extends HttpServlet {

	static boolean checkForInstance(int id) throws IOException, SQLException, ParseException {

     JSONObject outobj = new JSONObject();
		
		FileReader inputfile=new FileReader("db_config.properties");  
	      
	    Properties props=new Properties();  
	    props.load(inputfile);  
		boolean pharos = false;
		
		DBConnection dbCon =  new DBConnection(props.getProperty("JDBC_URL"), props.getProperty("USERNAME"),
				props.getProperty("PASSWORD"));
		Connection con = dbCon.getConnection();
		Statement stmt;
		String query = "select * from generated_templates where metadatacategoryid = " + id;
		try {
			stmt = con.createStatement();
			try {
				ResultSet rs = stmt.executeQuery(query);
				if (rs.next() == false) {
					pharos = false;
				} else {
					pharos = true;

				}
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		return pharos;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		JSONObject outobj = new JSONObject();
		
		FileReader inputfile=new FileReader("db_config.properties");  
	      
	    Properties props=new Properties();  
	    props.load(inputfile);  
	    
		
		DBConnection dbCon =  new DBConnection(props.getProperty("JDBC_URL"), props.getProperty("USERNAME"),
				props.getProperty("PASSWORD"));
		Connection con = dbCon.getConnection();
		Statement stmt;
		Connection con2 = dbCon.getConnection();
		Statement stmt2;
		Connection con3 = dbCon.getConnection();
		Statement stmt3;
		GetTemplateSchema getSchema = new GetTemplateSchema();
		response.setContentType("application/json");
		Redirect template;
		String schema;
		String old_template_id;
		String[] command;
		String template_id = null;
		String folder_id = null;
		String category = request.getParameter("category");

		String auth = "Authorization: " + " apiKey "+ props.getProperty("apikey");

		String query = "select g.templateid from generated_templates g "
				+ " inner join metadata_category m on (m.metadatacategoryid = g.metadatacategoryid) "
				+ "    inner join project p on (p.projectid = m.projectid) " + "  where g.metadatacategoryid = "
				+ Integer.parseInt(category);
		try {
			stmt = con.createStatement();
			try {
				ResultSet rs = stmt.executeQuery(query);
				if (rs.next()) {

					template_id = rs.getString("templateid");
				} else {
					String query2 = "select folder_id from  metadata_category m  "
							+ "   inner join project p on (p.projectid = m.projectid)  "
							+ " where m.metadatacategoryid =  " + Integer.parseInt(category);
					try {
						stmt2 = con2.createStatement();

						try {
							ResultSet rs2 = stmt2.executeQuery(query2);
							if (rs2.next()) {
								folder_id = rs2.getString("folder_id");
							}
						} catch (SQLException e2) {
							e2.printStackTrace();
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		Generator gen = new Generator();

		if (template_id != null) {
			// update existing one
			old_template_id = "https://resource.metadatacenter.org/templates/"
					+ URLEncoder.encode(template_id, "UTF-8");
			// old_template_id =
			// "https://resource.metadatacenter.org/templates/"+
			// URLEncoder.encode(template_id, "UTF-8");
			schema = gen.Generator(category, template_id).toString();

			// System.out.println(schema);

			command = new String[] { "curl", "-X", "PUT", "--header", "Content-Type: application/json", "--header",
					"Accept: application/json", "--header", auth, "-d", schema, old_template_id };

		} else {

			old_template_id = folder_id;

			// curl -X POST --header 'Content-Type: application/json' --header
			// 'Accept: application/json' --header 'Authorization: apiKey
			// d2e0213b9e43206deb2c54e08e54082a904ffb4edb40296cf6e110811d29ae9b'
			// -d '{}'
			// 'https://resource.metadatacenter.org/templates?folder_id=https%3A%2F%2Frepo.metadatacenter.org%2Ffolders%2F8bc64ab5-df6b-48c8-8c61-6c016245918e'
			// old_template_id
			// ="https://resource.metadatacenter.org/templates?folder_id=https%3A%2F%2Frepo.metadatacenter.net%2Ffolders%2F73854b02-9bc9-4b50-aea2-1ff9b581701b";
			old_template_id = "https://resource.metadatacenter.org/templates?folder_id="
					+ URLEncoder.encode(old_template_id, "UTF-8");
			schema = gen.Generator(category, "").toString();
			// System.out.println(old_template_id);
			// System.out.println("*******"+old_template_id);
			command = new String[] { "curl", "-X", "POST", "--header", "Content-Type: application/json", "--header",
					"Accept: application/json", "--header", auth, "-d", schema, old_template_id };
			// System.out.println("*****"+command);
		}

		ProcessBuilder process = new ProcessBuilder(command).inheritIO();

		try {
			String curlfile = null;
			curlfile = "curl" + System.currentTimeMillis() + ".json";
			process.redirectOutput(new File(getServletContext().getRealPath("") + curlfile));
			process.start();
			JSONParser jsonParser = new JSONParser();

			try {
				Thread.sleep(6000);
				try (FileReader reader = new FileReader(getServletContext().getRealPath("") + curlfile)) {
					// Read JSON file
					Object obj = jsonParser.parse(reader);

					JSONObject jb = (JSONObject) obj;
					System.out.println(jb);
					String schemaj = getSchema.GetJsonSchema(jb.get("@id").toString());
					if (schemaj.length() > 10) {
						if (checkForInstance(Integer.parseInt(category))) {
							try {

								System.out.println(jb.get("@id"));
								stmt3 = con3.createStatement();
								String insertQuerry = "UPDATE generated_templates SET templateid ='" + jb.get("@id")
										+ "', jsonschema = '" + getSchema.GetJsonSchema(jb.get("@id").toString())
										+ "' WHERE metadatacategoryid =" + Integer.parseInt(category);
								System.out.println(insertQuerry);
								stmt3.executeUpdate(insertQuerry);
								outobj.put("templat_id", jb.get("@id"));
							} catch (SQLException e3) {
								e3.printStackTrace();
							}
						} else {
							stmt3 = con3.createStatement();
							String insertQuerry = "insert into generated_templates (metadatacategoryid,templateid,jsonschema) values ("
									+ Integer.parseInt(category) + ",'" + jb.get("@id") + "','"
									+ getSchema.GetJsonSchema(jb.get("@id").toString()) + "')";
							System.out.println(insertQuerry);
							stmt3.execute(insertQuerry);
							outobj.put("templat_id", jb.get("@id"));
						}
					}

					response.getWriter().print(outobj);
					response.flushBuffer();

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}