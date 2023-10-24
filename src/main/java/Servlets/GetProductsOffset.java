package Servlets;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import Database.DomMebeli;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet({"/GetOffset"})
public class GetProductsOffset extends HttpServlet {
	private String search;
	private int productType;
	private int showcaseSize;

	private List<Integer> offsetBtnIndex;
	
	private PrintWriter writer;
	
	private Connection conn;
	private PreparedStatement statement;
	private ResultSet resSet;
	
	private void getOffset() throws Exception {
		String sql = "SELECT COUNT(*) FROM product";
		double productsCount;
		int offsetCount;
		
		if (search.length() > 0) {
			sql += " WHERE name = \"%s\"";
			sql = String.format(sql, search);
		}
		
		if (productType != -1) {
			sql += " WHERE type_id = \"%s\"";
			sql = String.format(sql, productType);
		}
		
		conn = DomMebeli.connect();
		statement = conn.prepareStatement(sql);
		resSet = statement.executeQuery();
		
		resSet.next();
		
		productsCount = resSet.getInt(1);
		offsetCount = (int)Math.ceil(productsCount / showcaseSize);
		
		int i = 0;
		while (i < offsetCount) {
			offsetBtnIndex.add(i);
			i++;
		}
		
		JSONArray result = new JSONArray(offsetBtnIndex);
		writer.print(result);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException {
		response.setContentType("text/html");
		writer = response.getWriter();
		
		try {
			offsetBtnIndex = new ArrayList<>();
			JSONObject filters = new JSONObject(request.getParameter("filters"));
		
			search = filters.getString("search");
			productType = filters.getInt("productType");
			showcaseSize = filters.getInt("showcaseSize");
			
			getOffset();
		} catch (Exception ex) {
			writer.print(ex.getMessage());
		}
	}
}