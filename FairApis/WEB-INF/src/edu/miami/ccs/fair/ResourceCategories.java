package edu.miami.ccs.fair;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ResourceCategories extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		out.println("{");
		out.println("\"data\":[{");
		out.println("\"endpointName\":\"reagents\",");
		out.println("\"Name\":\"Dry Reagents\"");
		out.println("},");
		out.println("{");
		out.println("\"endpointName\":\"datasets\",");
		out.println("\"Name\":\"Datasets\"");
		out.println("},");
		out.println("{");
		out.println("\"endpointName\":\"target\",");
		out.println("\"Name\":\"Targets\"");
		out.println("}");
		out.println("]}");
	}

}
