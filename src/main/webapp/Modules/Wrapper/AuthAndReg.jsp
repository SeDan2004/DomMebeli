<%@page language="Java" contentType="text/html; charset=UTF-8" %>

<link rel="stylesheet" href="./Static/Wrapper/AuthAndReg/AuthAndReg.css">

<div class="auth_and_reg_form">
	<div class="auth_and_reg_header">
		<div class="reg_btn">
			<p>Регистрация</p>
		</div>
			
		<div class="auth_btn not_active">
			<p>Авторизация</p>
		</div>
	</div>
		
	<div class="auth_and_reg_content">
		<div class="reg">
			<div class="reg_row1">
				<div class="reg_login_box">
					<div class="reg_txt_and_svg">
						<p>Логин</p>
						<%@include file="../../Icons/Пользователь.svg" %>
					</div>
						
					<input type="text" class="reg_input reg_login_inp">
				</div>
					
				<div class="reg_pass_box">
					<div class="reg_txt_and_svg">
						<p>Пароль</p>
						<%@include file="../../Icons/Пароль.svg" %>
					</div>
						
					<input type="password" class="reg_input reg_pass_inp">
				</div>
			</div>
				
			<div class="reg_row2">
				<div class="reg_email_box">
					<div class="reg_txt_and_svg">
						<p>Эл.адрес</p>
						<%@include file="../../Icons/Электронный адрес.svg" %>
					</div>
						
					<input type="text" class="reg_input reg_email_inp">
				</div>
					
				<div class="reg_repeat_pass_box">
					<div class="reg_txt_and_svg">
						<p>Повторите пароль</p>
						<%@include file="../../Icons/Пароль.svg" %>
					</div>
						
					<input type="password" class="reg_input reg_repeat_pass_inp">
				</div>
			</div>
		</div>
			
		<div class="auth">
			<div class="auth_login_box">
				<div class="auth_txt_and_svg">
					<p>Логин</p>
					<%@include file="../../Icons/Пользователь.svg" %>
				</div>
				
				<input type="text" class="auth_input auth_login_inp">
			</div>
			
			<div class="auth_pass_box">
				<div class="auth_txt_and_svg">
					<p>Пароль</p>
					<%@include file="../../Icons/Пароль.svg" %>
				</div>
				
				<input type="password" class="auth_input auth_pass_inp">
			</div>
			
			<div class="remember_me_box">
				<p>Запомнить меня</p>
				<input type="checkbox" class="auth_input auth_remember_me_inp">
			</div>
		</div>
			
		<div class="forgot_pass">
			<div class="fp_row1">
				<div class="fp_email_box">
					<div class="fp_txt_and_svg">
						<p>Эл.адрес</p>
						<%@include file="../../Icons/Электронный адрес.svg" %>
					</div>
					
					<input type="text" class="fp_input fp_email_inp" tabindex="-1">
				</div>
				
				<div class="fp_pass_box">
					<div class="fp_txt_and_svg">
						<p>Новый пароль</p>
						<%@include file="../../Icons/Пароль.svg" %>
					</div>
					
					<input type="password" class="fp_input fp_pass_inp" tabindex="-1">
				</div>
			</div>
			
			<div class="fp_row2">
				<div class="fp_token_box">
					<div class="fp_txt_and_svg">
						<p>Токен</p>
						<%@include file="../../Icons/Токен.svg" %>
					</div>
					
					<input type="text" class="fp_input fp_token_inp" tabindex="-1">
				</div>
				
				<div class="fp_repeat_pass_box">
					<div class="fp_txt_and_svg">
						<p>Повторите пароль</p>
						<%@include file="../../Icons/Пароль.svg" %>
					</div>
					
					<input type="password" class="fp_input fp_repeat_pass_inp" tabindex="-1">
				</div>
			</div>
		</div>
	</div>
		
	<div class="auth_and_reg_footer">
		<p class="forgot_pass_p">Забыли пароль?</p>
		
		<div class="auth_and_reg_footer_btns">		
			<div class="close">
				<p>Закрыть</p>
			</div>
			
			<div class="signin_form_btn">
				<p>Войти</p>
			</div>
			
			<div class="back">
				<p>Назад</p>
			</div>
			
			<div class="accept">
				<p>Подтвердить</p>
			</div>
		</div>
	</div>
</div>

<script src="./Static/Wrapper/AuthAndReg/AuthAndReg.js"></script>