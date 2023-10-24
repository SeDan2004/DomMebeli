package Servlets;

import java.io.IOException;
import java.io.PrintWriter;

import UserPack.Backet;
import UserPack.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet({"/AddProduct"})
public class AddProductInBacket extends HttpServlet {
	private String backetKey;
	private int productId;
	
	public PrintWriter writer;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException {
		response.setContentType("text/html");
		writer = response.getWriter();
		
		try {
			Backet backet;
			
			backetKey = request.getParameter("backetKey");
			productId = Integer.parseInt(request.getParameter("productId"));
			
			if (!backetKey.equals("null")) {
				backet = Backet.checkBacketKey(backetKey);
				backet.addProductInBacket(productId);
			} else {
				HttpSession session = request.getSession();
				User user = (User)session.getAttribute("User");
				
				if (user != null) {
					if (user.backets.size() == 0) {
						backet = Backet.createBacket(user.getId());
						backet.addProductInBacket(productId);
						user.backets.add(backet);
					} else {
						backet = Backet.getActiveStatusBacket(user.backets);
						backet.addProductInBacket(productId);
					}
				} else {
					backetKey = Backet.generateBacketKey();
				
					backet = Backet.createBacket(backetKey);
					backet.addProductInBacket(productId);
					
					writer.print(backetKey);
				}
			}
		} catch (Exception ex) {
			writer.print(ex.getMessage());
		}
	}
}