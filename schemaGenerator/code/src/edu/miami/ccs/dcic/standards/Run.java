package edu.miami.ccs.dcic.standards;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class Run {

	static JSONObject indp = new JSONObject();

	public static void main(String[] args) throws InterruptedException, IOException {
		
//		System.out.println("Please provide the file path (ex: /Downloads/AntibodyFields.csv): ");
//		// controlled voc
//		// names, Description, type
//		Scanner in = new Scanner(System.in);
//
//		String SAMPLE_CSV_FILE_PATH = in.nextLine();
//
//		System.out.println("Are you using the Jar to generate the template in CEDAR ? Yes/No ");
//		// controlled voc
//		// names, Description, type
//		Scanner cd = new Scanner(System.in);
//
//		String cedar = cd.nextLine();
//
//		System.out.println("Please provide the name of the entity (ex: Cell Line) ");
//
//		Scanner en = new Scanner(System.in);
//
//		String entityType = en.nextLine().toString();
//
//		System.out.println("Please provide the type of the entity ");
//
//		Scanner ty = new Scanner(System.in);
//
//		String type = ty.nextLine().toString();
//
//		System.out.println("Please provide the Description ");
//
//		Scanner des = new Scanner(System.in);
//
//		String description = des.nextLine().toString();
		
		Utils cu = new Utils();
		

		
		
		cu.individualProperties("CL_Alternative_ID","This field specifies the CLO, if available, or other common ID for the cell line or, if derived from another line, the CLO or other ID for the parent cell line. The parent cell line ID must be propagated to all cell lines derived from that parent line unless a distinct CLO has been assigned to the derived line.","","","Immortal Cell Line Cell","http://purl.obolibrary.org/obo/CLO_0000019","name", indp);
		cu.individualProperties("CL_Alternative_ID2","This field specifies the CLO, if available, or other common ID for the cell line or, if derived from another line, the CLO or other ID for the parent cell line. The parent cell line ID must be propagated to all cell lines derived from that parent line unless a distinct CLO has been assigned to the derived line.","","","Immortal Cell Line Cell","http://purl.obolibrary.org/obo/CLO_0000019","name", indp);

		
	
		
		System.out.println(indp);
//		
//		System.out.println(cu.setDescription(description));
//		
//		System.out.println(cu.setTitle(entityType));
		
//		CedarSchemaUtils cu = new CedarSchemaUtils();
//	
//		System.out.println(cu.propertyLabels("/Users/akoleti/Downloads/ESCells.csv"));
//		
//		System.out.println(cu.order("/Users/akoleti/Downloads/ESCells.csv"));
//		
//		System.out.println(cu.propertyDescriptions("/Users/akoleti/Downloads/ESCells.csv"));
//		
//		System.out.println(cu.pages());
		
		}
	
	 


}
