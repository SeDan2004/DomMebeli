<%@page language="Java" contentType="text/html; charset=UTF-8" %>

<%@page import="UserPack.User" %>
<%@page import="UserPack.Caesar" %>

<%
	User user = (User)session.getAttribute("User");
	Cookie[] cookie = request.getCookies();
	String login, pass;

	login = "";
	pass = "";
	
	for (int i = 0; i < cookie.length; i++) {
		String cookieName = cookie[i].getName();
		
		if (cookieName.equals("login")) {
			login = Caesar.caesar(cookie[i].getValue(), "decrypt");
		}
		
		if (cookieName.equals("pass")) {
			pass = Caesar.caesar(cookie[i].getValue(), "decrypt");
		}
	}
	
	if (!login.equals("") && !pass.equals("")) {
		session.setAttribute("User", User.checkUserInDb(login, pass));
		user = (User)session.getAttribute("User");
	}
%>