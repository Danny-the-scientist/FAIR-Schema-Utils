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

public class CellLineJsonGenerator {

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
	

		final String SAMPLE_CSV_FILE_PATH = "/Users/akoleti/Desktop/05_cellosurus_submission_final_new_name.csv";

		try (Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH))) {

			for (CSVRecord csvRecord : CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {
				String filename = null;

				BasicDBObject doc = new BasicDBObject();
				BasicDBObject documentBuilderDetail = new BasicDBObject();
				filename = "output_" + System.currentTimeMillis() + ".txt";
			
				out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(filename), true)), 10000);
				out.write(
						"{\"@context\": {  \"rdfs\": \"http://www.w3.org/2000/01/rdf-schema#\",  \"xsd\": \"http://www.w3.org/2001/XMLSchema#\",  \"pav\": \"http://purl.org/pav/\",  \"schema\": \"http://schema.org/\",  \"oslc\": \"http://open-services.net/ns/core#\",  \"skos\": \"http://www.w3.org/2004/02/skos/core#\",  \"rdfs:label\": {  	\"@type\": \"xsd:string\"  },  \"schema:isBasedOn\": {  	\"@type\": \"@id\"  },  \"schema:name\": {  	\"@type\": \"xsd:string\"  },  \"schema:description\": {  	\"@type\": \"xsd:string\"  },  \"pav:derivedFrom\": {  	\"@type\": \"@id\"  },  \"pav:createdOn\": {  	\"@type\": \"xsd:dateTime\"  },  \"pav:createdBy\": {  	\"@type\": \"@id\"  },  \"pav:lastUpdatedOn\": {  	\"@type\": \"xsd:dateTime\"  },  \"oslc:modifiedBy\": {  	\"@type\": \"@id\"  },  \"skos:notation\": {  	\"@type\": \"xsd:string\"  },  \"Sequence\": \"http://www.biopax.org/release/biopax-level3.owl#sequence\",  \"Vendor_cat\": \"http://purl.org/dc/elements/1.1/identifier\",  \"Disease_ID\": \"http://purl.org/dc/elements/1.1/description\",  \"CLO_ID\": \"http://purl.org/dc/elements/1.1/identifier\",  \"Precursor_cell\": \"http://purl.obolibrary.org/obo/CLO_0037226\",  \"Type\": \"http://purl.bioontology.org/ontology/SNOMEDCT/has_direct_morphology\",  \"Tissue\": \"http://purl.obolibrary.org/obo/CLO_0037227\",  \"Cellosaurus_ID\": \"http://purl.org/dc/elements/1.1/identifier\",  \"Species\": \"http://purl.obolibrary.org/obo/CLO_0037229\",  \"Vendor\": \"http://schema.org/vendor\",  \"Gene\": \"http://purl.bioontology.org/ontology/OMIM/GENESYMBOL\",  \"Target\": \"http://data.bioontology.org/ontologies/DTO\",  \"Disease_name\": \"http://purl.obolibrary.org/obo/CLO_0000167\",  \"CLO\": \"http://purl.org/dc/elements/1.1/title\",  \"Name\": \"https://schema.metadatacenter.org/properties/88bda78d-8c1a-4065-a2cc-2fb7256da8d8\",  \"RRID\": \"http://purl.org/dc/elements/1.1/identifier\",  \"Mod_type\": \"https://schema.metadatacenter.org/properties/bccea88c-5b4f-4f6a-9773-8270b3d1d2a2\",  \"Mouse_ID\": \"https://schema.metadatacenter.org/properties/96d8d058-2227-4b7c-a2ca-2926080a0384\",  \"Repository\": \"https://schema.metadatacenter.org/properties/f4cc5bff-9692-4c9a-a33c-4131d42ac0ba\",  \"Repository page link\": \"https://schema.metadatacenter.org/properties/f983c282-620e-452a-9e3c-c87e965c2b84\",  \"Data page link\": \"https://schema.metadatacenter.org/properties/3efcc0c9-6142-44dd-a973-e0a9566574ec\",  \"Knockout\": \"https://schema.metadatacenter.org/properties/0355d984-6a36-4aa1-aefa-a1ffc5bf49fe\",  \"Vector_ID\": \"https://schema.metadatacenter.org/properties/61d6152e-3715-4878-b23b-a6f11c88ae5d\",  \"Target_family\": \"https://schema.metadatacenter.org/properties/330a9399-f81e-485e-b349-27dcecbdb1ff\",  \"Reporter_type\": \"https://schema.metadatacenter.org/properties/c0ed2405-115a-4b35-9598-dc97a74834fd\"	},"
								+ "\"Gene\":{\"@value\":\"" + csvRecord.get("Gene")+ "\"},"
								+ "\"Target\":{\"@value\":\"" + csvRecord.get("Target") + "\"},"
								+ "\"Reporter_type\":{\"@value\":\"" + csvRecord.get("Reporter_type")
								+ "\"}," + "\"Mod_type\":{\"@value\":\"" + csvRecord.get("Mod_type") + "\"}"
								+ ",\"Name\":{\"@value\":\"" + csvRecord.get("Name") + "\"},"
								+ "\"Disease_ID\":{\"@value\":\"" + csvRecord.get("Disease_ID") + "\"},"
								+ "\"Disease_name\":{\"@value\":\"" + csvRecord.get("Disease_name") + "\"},"
								+ "\"CLO_ID\":{\"@value\":\"" + csvRecord.get("CLO_ID") + "\"},"
								+ "\"CLO\":{\"@value\":\"" + csvRecord.get("CLO_ID") + "\"},"
								+ "\"Type\":{\"@value\":\"" + csvRecord.get("Type") + "\"},"
								+ "\"Tissue\":{\"@value\":\"" + csvRecord.get("Tissue") + "\"},"
								+ "\"Species\":{\"@value\":\"" + csvRecord.get("Species") + "\"},"
								+ "\"Vendor\":{\"@value\":\"" + csvRecord.get("Vendor") + "\"},"
								+ "\"Vendor_cat\":{\"@value\":\"" + csvRecord.get("Vendor_cat") + "\"},"
								+ "\"Sequence\":{\"@value\":\"" + csvRecord.get("Sequence") + "\"},"
								+ "\"Vector_ID\":{\"@value\":\"" + csvRecord.get("Vector_ID") + "\"},"
								+ "\"Mouse_ID\":{\"@value\":\"" + csvRecord.get("Mouse_ID") + "\"},"
								+ "\"Precursor_cell\":{\"@value\":\"" + csvRecord.get("Precursor_cell") + "\"},"
							    + "\"RRID\":{\"@value\":\"" + csvRecord.get("RRID") + "\"},"
								+ "\"Repository\":{\"@value\":\"" + csvRecord.get("Repository") + "\"},"
								+ "\"Repository page link\":{\"@value\":\"" + csvRecord.get("Repositor_page_link") + "\"},"
								+ "\"Data page link\":{\"@value\":null},"
								+ "\"Knockout\":{\"@value\":\"" + csvRecord.get("Knockout") + "\"},"
								+ "\"Target_family\":{\"@value\":null},"
								+ "\"Cellosaurus_ID\":{\"@value\":null},"
		
								+ "\"schema:isBasedOn\":\"https://repo.metadatacenter.org/templates/4531fee7-e9d5-4097-ba00-0f1348ac21e9\",\"schema:name\":\"Cell Line\",\"schema:description\":\"Cell Line Metadata Standards\",\"pav:createdOn\":\"2019-10-22T09:20:32-0700\",\"pav:createdBy\":\"https://metadatacenter.org/users/200114c6-0f5d-4d09-bd8c-d95ea607c545\",\"pav:lastUpdatedOn\":\"2019-10-22T09:20:32-0700\",\"oslc:modifiedBy\":\"https://metadatacenter.org/users/200114c6-0f5d-4d09-bd8c-d95ea607c545\"}");
				out.close();
				String[] command;
				command = new String[] { "curl", "-H", "Content-Type: application/json", "-H",
						"Authorization: apiKey d2e0213b9e43206deb2c54e08e54082a904ffb4edb40296cf6e110811d29ae9b", "-X",
						"POST", "--data-binary", "@" + filename,
						"https://resource.metadatacenter.org/template-instances?folder_id=https://repo.metadatacenter.org/folders/bea02f48-8cf2-4f96-9efa-ce576915a20d" };
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
						ir.insertDocument(csvRecord.get("Gene"),csvRecord.get("Name"),"IDG Cells","zicheng.hu@ucsf.edu",curlfile);

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
