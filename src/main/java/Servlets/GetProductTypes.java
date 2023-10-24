package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import Database.DomMebeli;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet({"/GetProductTypes"})
public class GetProductTypes extends HttpServlet {
	private PrintWriter writer;
	
	private List<String> productTypes = new ArrayList<>();
	
	private Connection conn;
	private PreparedStatement statement;
	private ResultSet resSet;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException {
		response.setContentType("text/html");
		writer = response.getWriter();
		
		try {
			String sql = "SELECT type FROM product_type";
			
			conn = DomMebeli.connect();
			statement = conn.prepareStatement(sql);
			resSet = statement.executeQuery();
			
			while (resSet.next()) {
				productTypes.add(resSet.getString("type"));
			}
			
			JSONArray productTypesArr = new JSONArray(productTypes);
			writer.print(productTypesArr);
			
			productTypes.clear();
		} catch (Exception ex) {
			writer.print(ex.getMessage());
		}
	}
	
}