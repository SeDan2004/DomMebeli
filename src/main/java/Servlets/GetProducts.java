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
import UserPack.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet({"/GetProducts"})
public class GetProducts extends HttpServlet {
	private int offset;
	
	private String search;
	private int productType;
	private int showcaseSize;
	
	private List<String[]> products;
	
	private PrintWriter writer;
	
	private Connection conn;
	private PreparedStatement statement;
	private ResultSet resSet;
	
	private HttpSession session;
	private String backetKey;
	
	private void getProducts() throws Exception {
		String sql = "SELECT id, name, price, img_src FROM product";
				
		if (search.length() > 0) {
			sql += " WHERE name LIKE \"%" + search + "%\"";
		}
		
		if (productType != -1) {
			sql += " WHERE type_id = \"" + productType + "\"";
		}
				
		offset *= showcaseSize;
		
		sql += " LIMIT " + showcaseSize + " OFFSET " + offset;
		
		conn = DomMebeli.connect();
		statement = conn.prepareStatement(sql);
		resSet = statement.executeQuery();
		
		User user = (User)session.getAttribute("User");
		
		while (resSet.next()) {
			sql = "SELECT COUNT(bp.product_id) FROM backets b JOIN backet_products bp ON " + 
		          "b.id = bp.backet_id WHERE " + 
				  "b.user_id = ? AND b.status = 1 AND bp.product_id = ?";
			
			String id, name, price, imgSrc, inBacket;
			ResultSet productInBacketRes;
			
			id = resSet.getInt("id") + "";
			name = resSet.getString("name");
			price = resSet.getDouble("price") + "";
			imgSrc = resSet.getString("img_src");
			inBacket = "false";
			
			if (!backetKey.equals("null") || user != null) {
				
				statement = conn.prepareStatement(sql);
				
				if (!backetKey.equals("null")) {
					statement.setString(1, backetKey);
				}
				
				if (user != null) {
					statement.setString(1, user.getId());
				}
			
				statement.setString(2, id);
			
				productInBacketRes = statement.executeQuery();
				productInBacketRes.next();
			
				inBacket = productInBacketRes.getInt(1) + "";
			
				if (inBacket.equals("0")) inBacket = "false";
				if (inBacket.equals("1")) inBacket = "true";
			}
			
			String[] productInf = {
				id, 
				name, 
				price, 
				imgSrc, 
				inBacket
			};
			
			products.add(productInf);
		}
		
		JSONArray result = new JSONArray(products);
		writer.print(result);
		
		products.clear();
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException {
		response.setContentType("text/html");
		writer = response.getWriter();
		
		session = request.getSession();
		
		try {
			offset = Integer.parseInt(request.getParameter("offset"));
			products = new ArrayList<>();
			
			JSONObject filters = new JSONObject(request.getParameter("filters"));
			
			search = filters.getString("search");
			productType = filters.getInt("productType");
			showcaseSize = filters.getInt("showcaseSize");
			
			backetKey = request.getParameter("backetKey");
			
			getProducts();
		} catch (Exception ex) {
			ex.printStackTrace();
			writer.print(ex.getMessage());
		}
	}
}