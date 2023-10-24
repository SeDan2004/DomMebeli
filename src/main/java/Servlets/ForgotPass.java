package Servlets;

import java.io.IOException;
import java.io.PrintWriter;

import UserPack.MailSender;
import UserPack.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet({"/ForgotPass"})
public class ForgotPass extends HttpServlet {
	
	private PrintWriter writer;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException {
		response.setContentType("text/html");
		writer = response.getWriter();
		
		try {
			String email, token, pass, parameterName;
			HttpSession session = request.getSession();
			
			parameterName = request.getParameterNames().nextElement();
						
			if (parameterName.equals("email")) {
				email = request.getParameter("email");
				token = MailSender.sendToken(email);
				
				session.setAttribute("token", token);
			}
			
			if (parameterName.equals("token")) {
				String emailToken;
				
				emailToken = (String)session.getAttribute("token");
				token = request.getParameter("token");
								
				if (!emailToken.equals(token)) {
					writer.print("Токены не совпадают!");
				}
			}
			
			if (parameterName.equals("pass")) {
				pass = request.getParameter("pass");
				email = request.getParameter("email");
				
				if (email != null) {
					User.updateUserPass(pass, email);
					session.removeAttribute("token");
					
					writer.print("Пароль был успешно изменён!");
				}
			}
		} catch (Exception ex) {
			writer.println(ex.getMessage());
		}
	}
	
}