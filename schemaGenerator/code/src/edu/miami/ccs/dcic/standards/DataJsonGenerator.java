package edu.miami.ccs.dcic.standards;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class DataJsonGenerator {

	public static void main(String[] args) throws IOException, ParseException {
		ServerAddress serverAddress = new ServerAddress("localhost", 27017);
		// final MongoCredential creds =
		// MongoCredential.createCredential("SigC","SignatureData","SigC".toCharArray());
		MongoClient mongoClient = new MongoClient(serverAddress);
		DB db = mongoClient.getDB("dss");
		DBCollection coll = db.getCollection("rss");
		BufferedWriter out;
		String pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());
	

		final String SAMPLE_CSV_FILE_PATH = "/Users/akoleti/Downloads/dataset_m.csv";

		try (Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH))) {

			for (CSVRecord csvRecord : CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {
				String filename = null;

				BasicDBObject doc = new BasicDBObject();
				BasicDBObject documentBuilderDetail = new BasicDBObject();
				filename = "output_" + System.currentTimeMillis() + ".txt";
			
				out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(filename), true)), 10000);
				out.write(
						"{\"@context\":{\"rdfs\":\"http://www.w3.org/2000/01/rdf-schema#\",\"xsd\":\"http://www.w3.org/2001/XMLSchema#\",\"pav\":\"http://purl.org/pav/\",\"schema\":\"http://schema.org/\",\"oslc\":\"http://open-services.net/ns/core#\",\"skos\":\"http://www.w3.org/2004/02/skos/core#\",\"rdfs:label\":{\"@type\":\"xsd:string\"},\"schema:isBasedOn\":{\"@type\":\"@id\"},\"schema:name\":{\"@type\":\"xsd:string\"},\"schema:description\":{\"@type\":\"xsd:string\"},\"pav:derivedFrom\":{\"@type\":\"@id\"},\"pav:createdOn\":{\"@type\":\"xsd:dateTime\"},\"pav:createdBy\":{\"@type\":\"@id\"},\"pav:lastUpdatedOn\":{\"@type\":\"xsd:dateTime\"},\"oslc:modifiedBy\":{\"@type\":\"@id\"},\"skos:notation\":{\"@type\":\"xsd:string\"},\"Gene Symbol\":\"http://purl.bioontology.org/ontology/OMIM/GENESYMBOL\",\"Title\":\"https://schema.metadatacenter.org/properties/a78553f1-a953-4ce7-9791-14f3ad3710f4\",\"Provider_institution\":\"https://schema.metadatacenter.org/properties/588e7810-c158-49d1-8099-4585ae184230\",\"Description\":\"https://schema.metadatacenter.org/properties/08e6953d-6925-40c5-b7ba-eda86bb21055\",\"PI\":\"https://schema.metadatacenter.org/properties/f882e69f-8afc-40ee-b654-f7c892580516\",\"Authors\":\"https://schema.metadatacenter.org/properties/6b3d030e-4941-4e9b-943d-84b656fff7f5\",\"Assay_ID\":\"https://schema.metadatacenter.org/properties/c31c6ed9-79d3-4e1a-b4ad-f2ab42ef9f40\",\"Data_format\":\"https://schema.metadatacenter.org/properties/a3420af5-d5b7-4e76-ace3-525979049fe8\",\"Data_repository\":\"https://schema.metadatacenter.org/properties/87df538d-dc3f-4227-91b6-22c1055332eb\",\"Data_link\":\"https://schema.metadatacenter.org/properties/bd23a22f-a206-46a9-a955-42e07570f054\",\"Endpoint\":\"https://schema.metadatacenter.org/properties/18238cc2-3602-4d41-b5c3-fe1b0777718b\",\"Endpoint_detection\":\"https://schema.metadatacenter.org/properties/399d55ab-4350-4cfa-8f08-acd854205e25\",\"Release date\":\"https://schema.metadatacenter.org/properties/14eb6644-f93b-440e-af2b-61a9722bd073\",\"Repository\":\"https://schema.metadatacenter.org/properties/f85c63bc-ad50-4b9a-8848-3cec1fe834fa\",\"Dataset ID\":\"https://schema.metadatacenter.org/properties/210f320e-899d-48dd-8ff5-f3a125d8bd4b\"},"
								+ "\"Gene Symbol\":{\"@id\":\"http://druggablegenome.net/" + csvRecord.get("Gene")
								+ "\"" + "," + "\"rdfs:label\":\"" + csvRecord.get("Gene") + "\"},"
								+ "\"Title\":{\"@value\":\"" + csvRecord.get("Title") + "\"},"
								+ "\"Provider_institution\":{\"@value\":\"" + csvRecord.get("Provider_institution")
								+ "\"}," + "\"Description\":{\"@value\":\"" + csvRecord.get("Description") + "\"}"
								+ ",\"PI\":{\"@value\":\"" + csvRecord.get("PI") + "\"},"
								+ "\"Authors\":{\"@value\":null},\"Assay_ID\":{\"@value\":null},\"Data_format\":{\"@value\":null},"
								+ "\"Data_repository\":{\"@value\":\"" + csvRecord.get("Data_repository") + "\"},"
								+ "\"Data_link\":{\"@value\":\"" + csvRecord.get("Data_link") + "\"},"
								+ "\"Endpoint\":{\"@value\":null},\"Endpoint_detection\":{\"@value\":null},\"Release date\":{\"@value\":null},"
								+ "\"Repository\":{\"@value\":\"" + csvRecord.get("Repository") + "\"},"
								+ "\"Dataset ID\":{\"@value\":null},"
								+ "\"schema:isBasedOn\":\"https://repo.metadatacenter.org/templates/83a0b7c2-88be-46fc-b1f9-8aa1d5fc6578\",\"schema:name\":\"Data metadata\",\"schema:description\":\"Data Metadata Standards\",\"pav:createdOn\":\"2019-06-12T09:20:32-0700\",\"pav:createdBy\":\"https://metadatacenter.org/users/200114c6-0f5d-4d09-bd8c-d95ea607c545\",\"pav:lastUpdatedOn\":\"2019-06-12T09:20:32-0700\",\"oslc:modifiedBy\":\"https://metadatacenter.org/users/200114c6-0f5d-4d09-bd8c-d95ea607c545\"}");
				out.close();
				String[] command;
				command = new String[] { "curl", "-H", "Content-Type: application/json", "-H",
						"Authorization: apiKey d2e0213b9e43206deb2c54e08e54082a904ffb4edb40296cf6e110811d29ae9b", "-X",
						"POST", "--data-binary", "@" + filename,
						"https://resource.metadatacenter.org/template-instances" };
				ProcessBuilder process = new ProcessBuilder(command).inheritIO();
				Process p;
				try {
					String curlfile = null;
					curlfile = "curl" + System.currentTimeMillis() + ".json";
					process.redirectOutput(new File(curlfile));
					p = process.start();
					
					

					InsertDocs ir = new InsertDocs();
					try{
					    Thread.sleep(6000);
//						ir.insertDocument(csvRecord.get("Gene"),csvRecord.get("Title"),"Dataset","matthew.berginski@gmail.com",curlfile);

					    // Then do something meaningful...
					}catch(InterruptedException e){
					    e.printStackTrace();
					}
					

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

}
