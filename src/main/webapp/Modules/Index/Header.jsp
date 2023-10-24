<%@page language="Java" contentType="text/html; charset=UTF-8" %>
<%@page import="UserPack.User" %>

<%
	User user = (User)session.getAttribute("User");
%>

<link rel="stylesheet" href="./Static/Index/Header/Header.css">

<header>
	<div class="header_box1">
		<div class="brand_name">
			<h1>Дом мебели</h1>
			<%@include file="/Icons/Дом.svg" %>
		</div>
		
		<div class="header_btn_box1">
			<p class="about_company">О компании</p>
			<p class="support">Тех.поддержка</p>
		</div>
	</div>
	
	<div class="header_box2">
		
		<%
			if (user != null) {
		%>
		
		<div class="profile_btn auth_true">
			<div class="profile_icon">
				<%@include file="/Icons/Личный-кабинет.svg" %>
			</div>
			
			<p>Кабинет</p>
		</div>
	
		<% } else {%>
		
		<div class="profile_btn">
			<div class="profile_icon">
				<%@include file="/Icons/Личный-кабинет.svg" %>
			</div>
			
			<p>Кабинет</p>
		</div>
	
		<div class="signin_btn">
			<div class="user_icon">
				<%@include file="/Icons/Пользователь.svg" %>
			</div>
			
			<p>Войти</p>
		</div>
		
		<% }%>
		
		<div class="backet_btn">
			<div class="backet_icon">
				<%@include file="/Icons/Корзина.svg" %>
			</div>
			
			<p>Корзина</p>
		</div>
	</div>
</header>

<script src="./Static/Index/Header/Header.js"></script>