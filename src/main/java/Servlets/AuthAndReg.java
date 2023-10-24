package Servlets;

import static UserPack.Caesar.caesar;

import java.io.IOException;
import java.io.PrintWriter;

import UserPack.Backet;
import UserPack.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet({"/AuthAndReg"})
public class AuthAndReg extends HttpServlet {
	
	private PrintWriter writer;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
	throws IOException, ServletException {
		response.setContentType("text/html");
		
		writer = response.getWriter();
		
		try {
			String login, pass, email, rememberMe, type, backetKey;
			
			HttpSession session = request.getSession();
			
			type = request.getParameter("type");
			backetKey = request.getParameter("backetKey");
		
			if (type.equals("reg")) {
				login = request.getParameter("login");
				pass = request.getParameter("pass");
				email = request.getParameter("email");
			
				User user = User.addUser(login, pass, email);
				
				if (!backetKey.equals("null")) {
					Backet backetWithBacketKey = Backet.checkBacketKey(backetKey);
					
					backetWithBacketKey.updateBacketId(user.getId(), backetKey);
					user.backets.add(backetWithBacketKey);
				}
				
				session.setAttribute("User", user);
			}
			
			if (type.equals("auth")) {
				login = request.getParameter("login");
				pass = request.getParameter("pass");
				rememberMe = request.getParameter("rememberMe");
				
				if (rememberMe.equals("true")) {
					Cookie userLogin, userPass;
					userLogin = new Cookie("login", caesar(login, "encrypt"));
					userPass = new Cookie("pass", caesar(pass, "encrypt"));
					
					userLogin.setMaxAge(7 * 24 * 3600);
					userPass.setMaxAge(7 * 24 * 3600);
					
					response.addCookie(userLogin);
					response.addCookie(userPass);
				}
				
				User user = User.checkUserInDb(login, pass);
								
				if (!backetKey.equals("null")) {
					Backet backetWithBacketKey = Backet.checkBacketKey(backetKey);
					
					if (user.backets.size() != 0) {
						Backet activeBacket;
						
						activeBacket = Backet.getActiveStatusBacket(user.backets);
						activeBacket.setStatusBacket(false);
					}
					
					backetWithBacketKey.updateBacketId(user.getId(), backetKey);
					user.backets.add(backetWithBacketKey);
				}
				
				session.setAttribute("User", user);
			}
		} catch (Exception ex) {
			writer.print(ex.getMessage());
		}
	}
	
}