package edu.miami.ccs.dcic.standards;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class Generator {
	Date now = new Date();
	private static BufferedWriter out;

	public static void main(String[] args) throws IOException {
		String pattern = "yyyy-MM-dd'T'HH:mm:ss-SSS";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());

		

		System.out.println("Please provide the file path (ex: /Downloads/AntibodyFields.csv): ");
		// controlled voc
		// names, Description, type
		Scanner in = new Scanner(System.in);

		String SAMPLE_CSV_FILE_PATH = in.nextLine();

		System.out.println("Are you using the Jar to generate the template in CEDAR ? Yes/No ");
		// controlled voc
		// names, Description, type
		Scanner cd = new Scanner(System.in);

		String cedar = cd.nextLine();

		System.out.println("Please provide the name of the entity (ex: Cell Line) ");

		Scanner en = new Scanner(System.in);

		String entityType = en.nextLine().toString();

		System.out.println("Please provide the type of the entity ");

		Scanner ty = new Scanner(System.in);

		String type = ty.nextLine().toString();

		System.out.println("Please provide the Description ");

		Scanner des = new Scanner(System.in);

		String description = des.nextLine().toString();

		// String SAMPLE_CSV_FILE_PATH = "/Downloads/CellLineFields.csv";

		String filename = "output_" + System.currentTimeMillis() + ".txt";
		out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(filename), true)), 10000);

		out.write("{" + "	\"@type\": " + "\"" + type + "\","
				+ "\"@context\": {\"xsd\": \"http://www.w3.org/2001/XMLSchema#\","
				+ "\"pav\": \"http://purl.org/pav/\"," + "\"bibo\": \"http://purl.org/ontology/bibo/\","
				+ "\"oslc\": \"http://open-services.net/ns/core#\"," + "\"schema\": \"http://schema.org/\","
				+ "\"schema:name\": {	\"@type\": \"xsd:string\"},"
				+ "\"schema:description\": {	\"@type\": \"xsd:string\"},"
				+ "\"pav:createdOn\": {	\"@type\": \"xsd:dateTime\"}," + "\"pav:createdBy\": {	\"@type\": \"@id\"},"
				+ "\"pav:lastUpdatedOn\": {	\"@type\": \"xsd:dateTime\"},"
				+ "\"oslc:modifiedBy\": {	\"@type\": \"@id\"}" + "},\"type\": \"object\",");
		out.write("\"title\":\"" + entityType + "   schema\",");
		out.write("\"description\":\"" + description + "\",");

		try (Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));

				Reader reader2 = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));
				Reader reader3 = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));
				Reader reader4 = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));
				Reader reader5 = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));
				Reader reader6 = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));

				CSVParser totalRecords = new CSVParser(reader2,
						CSVFormat.DEFAULT.withHeader().withIgnoreHeaderCase().withSkipHeaderRecord());

		) {
			int totalC = totalRecords.getRecords().size();

			if (cedar.equalsIgnoreCase("yes")) {

				out.write("\"_ui\": {" + "	\"pages\": []," + "\"propertyLabels\": {");
				for (CSVRecord csvRecord : CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader4)) {
					out.write("\"" + csvRecord.get("Property") + "\": \"" + csvRecord.get("Property") + "\"");
					if (csvRecord.getRecordNumber() < totalC) {
						out.write(",");
					}
				}
				out.write("},");
				out.write("\"propertyDescriptions\": {");
				for (CSVRecord csvRecord : CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader6)) {
					out.write("\"" + csvRecord.get("Property") + "\": \"" + csvRecord.get("Description") + "\"");
					if (csvRecord.getRecordNumber() < totalC) {
						out.write(",");
					}
				}
				out.write("},");
				out.write("\"order\": [");
				for (CSVRecord csvRecord : CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader5)) {
					out.write("\"" + csvRecord.get("Property") + "\"");
					if (csvRecord.getRecordNumber() < totalC) {
						out.write(",");
					}
				}

				out.write("]" + "},");
			}

			out.write("\"properties\": {\"@context\":  {");
			out.write(
					"\"type\": \"object\",\"properties\": {\"rdfs\": {\"type\": \"string\",\"format\": \"uri\",\"enum\": [\"http://www.w3.org/2000/01/rdf-schema#\"]},\"xsd\": {\"type\": \"string\",\"format\": \"uri\",\"enum\": [\"http://www.w3.org/2001/XMLSchema#\"]},\"pav\": {\"type\": \"string\",\"format\": \"uri\",\"enum\": [\"http://purl.org/pav/\"]},\"schema\": {\"type\": \"string\",\"format\": \"uri\",\"enum\": [\"http://schema.org/\"]},\"oslc\": {\"type\": \"string\",\"format\": \"uri\",\"enum\": [\"http://open-services.net/ns/core#\"]},\"skos\": {\"type\": \"string\",\"format\": \"uri\",\"enum\": [\"http://www.w3.org/2004/02/skos/core#\"]},\"rdfs:label\": {\"type\": \"object\",\"properties\": {\"@type\": {\"type\": \"string\",\"enum\": [\"xsd:string\"]}}},\"schema:isBasedOn\": {\"type\": \"object\",\"properties\": {\"@type\": {\"type\": \"string\",\"enum\": [\"@id\"]}}},\"schema:name\": {\"type\": \"object\",\"properties\": {\"@type\": {\"type\": \"string\",\"enum\": [\"xsd:string\"]}}},\"schema:description\": {\"type\": \"object\",\"properties\": {\"@type\": {\"type\": \"string\",\"enum\": [\"xsd:string\"]}}},\"pav:createdOn\": {\"type\": \"object\",\"properties\": {\"@type\": {\"type\": \"string\",\"enum\": [\"xsd:dateTime\"]}}},\"pav:createdBy\": {\"type\": \"object\",\"properties\": {\"@type\": {\"type\": \"string\",\"enum\": [\"@id\"]}}},\"pav:lastUpdatedOn\": {\"type\": \"object\",\"properties\": {\"@type\": {\"type\": \"string\",\"enum\": [\"xsd:dateTime\"]}}},\"oslc:modifiedBy\": {\"type\": \"object\",\"properties\": {\"@type\": {\"type\": \"string\",\"enum\": [\"@id\"]}}},\"skos:notation\": {\"type\": \"object\",\"properties\": {\"@type\": {\"type\": \"string\",\"enum\": [\"xsd:string\"]}}},");

			for (CSVRecord csvRecord : CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {
				if (!csvRecord.get("Schema/BioSchemas.org General Representation (closest possible)").isEmpty()) {
					String[] scp = csvRecord.get("Schema/BioSchemas.org General Representation (closest possible)")
							.split(";");
					out.write("\"" + csvRecord.get("Property") + "\": { \"enum\": [\""
							+ csvRecord.get("Object Mapping URL") + "\",");
					for (int i = 0; i < scp.length; i++) {
						out.write("\"https://schema.org/" + scp[i] + "\"");
						if (i < scp.length - 1) {
							out.write(",");
						}
					}
					out.write("]}");
				} else {
					out.write("\"" + csvRecord.get("Schema/BioSchemas.org General Representation (closest possible)")
							+ "\": { \"enum\": [\"" + "https://schema.metadatacenter.org/properties/"
							+ entityType.toString().replaceAll(" ", "_") + csvRecord.get("Object Mapping URL")
							+ "\"]}");
				}
				if (csvRecord.getRecordNumber() < totalC) {
					out.write(",");
				} else {
					out.write(
							"},\"required\": [\"xsd\",\"pav\",\"schema\",\"oslc\",\"schema:isBasedOn\",\"schema:name\",\"schema:description\",\"pav:createdOn\",\"pav:createdBy\",\"pav:lastUpdatedOn\",\"oslc:modifiedBy\"],\"additionalProperties\": false},\"@id\": {\"type\": \"string\",\"format\": \"uri\"},\"@type\": {\"oneOf\": [{\"type\": \"string\",\"format\": \"uri\"},{\"type\": \"array\",\"minItems\": 1,\"items\": {\"type\": \"string\",\"format\": \"uri\"},\"uniqueItems\": true}]},\"schema:isBasedOn\": {\"type\": \"string\",\"format\": \"uri\"},\"schema:name\": {\"type\": \"string\",\"minLength\": 1},\"schema:description\": {\"type\": \"string\"},\"pav:createdOn\": {\"type\": [\"string\",\"null\"],\"format\": \"date-time\"},\"pav:createdBy\": {\"type\": [\"string\",\"null\"],\"format\": \"uri\"},\"pav:lastUpdatedOn\": {\"type\": [\"string\",\"null\"],\"format\": \"date-time\"},\"oslc:modifiedBy\": {\"type\": [\"string\",\"null\"],\"format\": \"uri\"},");
				}

			}

			for (CSVRecord csvRecord : CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader3)) {

				if (!csvRecord.get("Controlled Vocabulary Ontology (Name)").isEmpty()
						&& !csvRecord.get("Controlled Vocabulary Ontology (abb.)").isEmpty()
						&& !csvRecord.get("Controlled Vocabulary").isEmpty() && !csvRecord
								.get("Schema/BioSchemas.org General Representation (closest possible)").isEmpty()) {
					out.write("\"" + csvRecord.get("Property") + "\":{");
					if (csvRecord.get("Cardinality").contentEquals("Many")) {
						out.write("\"type\": \"array\",");
						out.write("\"minItems\": \"1\",");
						out.write("\"items\": {");
					}

					out.write("\"$schema\": \"http://json-schema.org/draft-04/schema#\",");
					// out.write("\"@id\": \"temp_" + System.currentTimeMillis()
					// + csvRecord.get("Field").toString().replaceAll(" ", "_")
					// + "\",");
					if (cedar.equalsIgnoreCase("yes")) {
						out.write("\"@type\": \"https://schema.metadatacenter.org/core/TemplateField\",");
					} else {
						out.write("\"@type\": \"https://schema.org/"
								+ csvRecord.get("Schema/BioSchemas.org General Representation (closest possible)").split(";")[0] + "\",");
					}
					out.write(
							"\"@context\": {\"xsd\": \"http://www.w3.org/2001/XMLSchema#\",\"pav\": \"http://purl.org/pav/\",\"bibo\": \"http://purl.org/ontology/bibo/\",\"oslc\": \"http://open-services.net/ns/core#\",\"schema\": \"http://schema.org/\",\"skos\": \"http://www.w3.org/2004/02/skos/core#\",\"schema:name\": {\"@type\": \"xsd:string\"},\"schema:description\": {\"@type\": \"xsd:string\"},\"skos:prefLabel\": {\"@type\": \"xsd:string\"},\"skos:altLabel\": {\"@type\": \"xsd:string\"},\"pav:createdOn\": {\"@type\": \"xsd:dateTime\"},\"pav:createdBy\": {\"@type\": \"@id\"},\"pav:lastUpdatedOn\": {\"@type\": \"xsd:dateTime\"},\"oslc:modifiedBy\": {\"@type\": \"@id\"}},\"type\": \"object\",");
					out.write("\"title\": \"" + csvRecord.get("Property") + "\",");
					out.write("\"description\": \"" + csvRecord.get("Description") + "\",");
					out.write("	\"_ui\": {\"inputType\":\"" + csvRecord.get("Schema.org Type(s)") + "\"},");
					out.write("\"_valueConstraints\": { \"requiredValue\": false ," + " \"branches\": [");
					//
					String[] ontologies = csvRecord.get("Controlled Vocabulary Ontology (abb.)").split(";");
					String[] source = csvRecord.get("Controlled Vocabulary Ontology (Name)").split(";");
					String[] uris = csvRecord.get("Controlled Vocabulary").split(";");
					String[] clsNames = csvRecord.get("Controlled Vocabulary Branch").split(";");
					for (int i = 0; i < ontologies.length; i++) {
						out.write("{\"acronym\":\"");
						out.write(ontologies[i] + "\",");
						out.write("\"source\": \"" + source[i] + "\",");
						out.write("\"uri\":\"");
						out.write(uris[i] + "\",\"name\": \"" + clsNames[i] + "\"," + "\"maxDepth\": 0");
						if (i < ontologies.length - 1) {
							out.write("},");
						}
					}
					out.write("}],\"multipleChoice\": false },");
					out.write(
							"\"properties\": {	\"@type\": {\"oneOf\": [{\"type\": \"string\",\"format\": \"uri\"},{\"type\": \"array\",\"minItems\": 1,\"items\": {\"type\": \"string\",\"format\": \"uri\"},\"uniqueItems\": true}]},\"@value\": {\"type\": [	\"string\" ,\"null\"		]	},\"rdfs:label\": {	\"type\": [\"string\",\"null\"	]}	"
									+ "},\"required\": [\"@value\"],");

					out.write(" \"schema:name\": \"" + csvRecord.get("Property") + "\",");
					out.write(" \"schema:description\": \"" + csvRecord.get("Description") + "\",");
					out.write(" \"pav:createdOn\": \"" + date + "\",");

					out.write(" \"pav:lastUpdatedOn\": \"" + date + "\",");

					out.write(" \"schema:schemaVersion\": \"" + "1.5.0" + "\",");
					out.write(" \"additionalProperties\": " + false + "");
					out.write("}");
					if (csvRecord.get("Cardinality").contentEquals("Many")) {
						out.write("}");
					}
				} else if (!csvRecord.get("Controlled Vocabulary").isEmpty()) {
					String[] controlledVoc = csvRecord.get("Controlled Vocabulary").split(";");
					out.write("\"" + csvRecord.get("Property") + "\":{");
					if (csvRecord.get("Cardinality").contentEquals("Many")) {
						out.write("\"type\": \"array\",");
						out.write("\"minItems\": \"1\",");
						out.write("\"items\": {");
					}
					out.write("\"type\": \"array\",\"minItems\": 1,\"items\": {");
					out.write(" \"$schema\": \"" + "http://json-schema.org/draft-04/schema#" + "\",");
					out.write(" \"type\": " + "\"object\"" + ",");
					if (cedar.equalsIgnoreCase("yes")) {
						out.write(" \"@id\": \"" + "https://repo.metadatacenter.org/template-fields/"
								+ System.currentTimeMillis() + "_"
								+ csvRecord.get("Property").toString().replaceAll(" ", "_") + "\",");
						out.write("\"@type\": \"https://schema.metadatacenter.org/core/TemplateField\",");
					} else {
						out.write("\"@type\": \"https://schema.org/"
								+ csvRecord.get("Schema/BioSchemas.org General Representation (closest possible)").split(";")[0] + "\",");
					}

					out.write(
							"\"@context\": {\"xsd\": \"http://www.w3.org/2001/XMLSchema#\",\"pav\": \"http://purl.org/pav/\",\"bibo\": \"http://purl.org/ontology/bibo/\",\"oslc\": \"http://open-services.net/ns/core#\",\"schema\": \"http://schema.org/\",\"skos\": \"http://www.w3.org/2004/02/skos/core#\",\"schema:name\": {\"@type\": \"xsd:string\"},\"schema:description\": {\"@type\": \"xsd:string\"},\"skos:prefLabel\": {\"@type\": \"xsd:string\"},\"skos:altLabel\": {\"@type\": \"xsd:string\"},\"pav:createdOn\": {\"@type\": \"xsd:dateTime\"},\"pav:createdBy\": {\"@type\": \"@id\"},\"pav:lastUpdatedOn\": {\"@type\": \"xsd:dateTime\"},\"oslc:modifiedBy\": {\"@type\": \"@id\"}},");
					out.write("\"title\": \"" + csvRecord.get("Property") + "\",");
					out.write("\"description\": \"" + csvRecord.get("Description") + " \",");
					out.write("	\"_ui\": {\"inputType\":" + "\"checkbox" + "\"},");
					out.write(
							"\"_valueConstraints\":  {	\"multipleChoice\": true, \"requiredValue\": false, \"literals\": [	");
					for (int i = 0; i < controlledVoc.length; i++) {
						out.write("{\"label\":\"" + controlledVoc[i] + "\"}");
						if (i < controlledVoc.length - 1) {
							out.write(",");
						} else {
							out.write(" 	]},");
						}
					}
					out.write(
							"\"properties\": {\"@type\": {\"oneOf\": [{\"type\": \"string\",\"format\": \"uri\"},{\"type\": \"array\",\"minItems\": 1,\"items\": {\"type\": \"string\",\"format\": \"uri\"},\"uniqueItems\": true}]},\"@value\": {\"type\": [\"string\",\"null\"]},\"rdfs:label\": {\"type\": [\"string\",\"null\"]}},");
					out.write("\"required\": [\"@value\"],");
					out.write(" \"additionalProperties\": " + false + ",");
					out.write(" \"pav:createdOn\": \"" + date + "\",");
					out.write(" \"pav:lastUpdatedOn\": \"" + date + "\",");
					if (cedar.equalsIgnoreCase("yes")) {
						out.write(" \"pav:createdBy\": \""
								+ "https://metadatacenter.org/users/200114c6-0f5d-4d09-bd8c-d95ea607c545" + "\",");

						out.write("\"oslc:modifiedBy\": \""
								+ "https://metadatacenter.org/users/200114c6-0f5d-4d09-bd8c-d95ea607c545" + "\",");
					}
					out.write(" \"schema:schemaVersion\": \"" + "1.5.0" + "\",");
					out.write(" \"schema:name\": \"" + csvRecord.get("Property") + "\",");
					out.write(" \"schema:description\": \"" + csvRecord.get("Description") + "\"");
					out.write("}}");
					if (csvRecord.get("Cardinality").contentEquals("Many")) {
						out.write("}");
					}
				} else {
					out.write("\"" + csvRecord.get("Property") + "\":{");
					if (csvRecord.get("Cardinality").contentEquals("Many")) {
						out.write("\"type\": \"array\",");
						out.write("\"minItems\": \"1\",");
						out.write("\"items\": {");
					}
					if (cedar.equalsIgnoreCase("yes")) {
						out.write("\"@type\": \"https://schema.metadatacenter.org/core/TemplateField\",");
					} else {
						out.write("\"@type\": \"https://schema.org/"
								+ csvRecord.get("Schema/BioSchemas.org General Representation (closest possible)").split(";")[0] + "\",");
					}
					out.write(
							"\"@context\": {\"xsd\": \"http://www.w3.org/2001/XMLSchema#\",\"pav\": \"http://purl.org/pav/\",\"bibo\": \"http://purl.org/ontology/bibo/\",\"oslc\": \"http://open-services.net/ns/core#\",\"schema\": \"http://schema.org/\",\"skos\": \"http://www.w3.org/2004/02/skos/core#\",\"schema:name\": {\"@type\": \"xsd:string\"},\"schema:description\": {\"@type\": \"xsd:string\"},\"skos:prefLabel\": {\"@type\": \"xsd:string\"},\"skos:altLabel\": {\"@type\": \"xsd:string\"},\"pav:createdOn\": {\"@type\": \"xsd:dateTime\"},\"pav:createdBy\": {\"@type\": \"@id\"},\"pav:lastUpdatedOn\": {\"@type\": \"xsd:dateTime\"},\"oslc:modifiedBy\": {\"@type\": \"@id\"}},\"type\": \"object\",");
					out.write("\"title\": \"" + csvRecord.get("Property") + "\",");
					out.write("\"description\": \"" + csvRecord.get("Description")
							+ " \",");
					out.write("	\"_ui\": {\"inputType\":\"" + csvRecord.get("Schema.org Type(s)") + "\"},");
					out.write("\"_valueConstraints\": { \"requiredValue\": false },");
					out.write(
							"\"properties\": {	\"@type\": {\"oneOf\": [{\"type\": \"string\",\"format\": \"uri\"},{\"type\": \"array\",\"minItems\": 1,\"items\": {\"type\": \"string\",\"format\": \"uri\"},\"uniqueItems\": true}]},\"@value\": {\"type\": [	\"string\" ,\"null\"		]	},\"rdfs:label\": {	\"type\": [\"string\",\"null\"	]}	"
									+ "},\"required\": [\"@value\"],");

					out.write(" \"schema:name\": \"" + csvRecord.get("Property") + "\",");
					out.write(" \"schema:description\": \"" + csvRecord.get("Description") + "\",");
					out.write(" \"pav:createdOn\": \"" + date + "\",");
					out.write(" \"schema:schemaVersion\": \"" + "1.5.0" + "\",");
					out.write(" \"pav:lastUpdatedOn\": \"" + date + "\",");
					if (cedar.equalsIgnoreCase("yes")) {
						out.write(" \"pav:createdBy\": \""
								+ "https://metadatacenter.org/users/200114c6-0f5d-4d09-bd8c-d95ea607c545" + "\",");

						out.write(" \"oslc:modifiedBy\": \""
								+ "https://metadatacenter.org/users/200114c6-0f5d-4d09-bd8c-d95ea607c545" + "\",");

						out.write(" \"@id\": \"" + "https://repo.metadatacenter.org/template-fields/"
								+ System.currentTimeMillis() + "_"
								+ csvRecord.get("Property").toString().replaceAll(" ", "_") + "\",");
					}
					out.write(" \"additionalProperties\": " + false + ",");
					out.write(" \"$schema\": \"" + "http://json-schema.org/draft-04/schema#" + "\"");
					out.write("}");
					if (csvRecord.get("Cardinality").contentEquals("Many")) {
						out.write("}");
					}

				}

				if (csvRecord.getRecordNumber() < totalC) {
					out.write(",");
				} else {
					out.write("},");
					out.write(
							"\"required\": [\"@context\",\"@id\",\"schema:isBasedOn\",\"schema:name\",\"schema:description\",\"pav:createdOn\",\"pav:createdBy\",\"pav:lastUpdatedOn\",\"oslc:modifiedBy\"],");
					if (cedar.equalsIgnoreCase("yes")) {		
					out.write( "\"pav:createdBy\": \"https://metadatacenter.org/users/200114c6-0f5d-4d09-bd8c-d95ea607c545\"," );
									out.write( "\"oslc:modifiedBy\": \"https://metadatacenter.org/users/200114c6-0f5d-4d09-bd8c-d95ea607c545\",");
					}
							out.write( "\"$schema\": \"http://json-schema.org/draft-04/schema#\",");
					out.write("\"pav:createdOn\": \"" + date + "\",");
					out.write("\"pav:lastUpdatedOn\": \"" + date + "\",");
					// "pav:createdOn": "2019-04-04T13:28:17-0700",
					out.write("\"schema:name\": \"" + entityType + "\"," + "\"schema:description\": \"" + entityType
							+ " Metadata Standards" + "\"," + "\"schema:schemaVersion\": \"1.5.0\","
							+ "\"additionalProperties\": false," + "\"pav:version\": \"0.0.1\","
							+ "\"bibo:status\": \"bibo:draft" + "\"}");
				}
			}
		}
		out.close();

	}

}
