package Servlets;

import static Database.DomMebeli.writer;
import static UserPack.Caesar.caesar;

import java.io.IOException;

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
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
	throws IOException, ServletException {
		response.setContentType("text/html");
		
		writer = response.getWriter();
		
		try {
			String login, pass, email, rememberMe, type;
			
			HttpSession session = request.getSession();
			
			type = request.getParameter("type");
		
			if (type.equals("reg")) {
				login = request.getParameter("login");
				pass = request.getParameter("pass");
				email = request.getParameter("email");
			
				session.setAttribute("User", User.addUser(login, pass, email));
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
				
				session.setAttribute("User", User.checkUserInDb(login, pass));
			}
		} catch (Exception ex) {
			ex.printStackTrace(writer);
		}
	}
	
}