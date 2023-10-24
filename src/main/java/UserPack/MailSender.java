package UserPack;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import Database.DomMebeli;

public class MailSender {
	
	private static Connection conn;
	private static PreparedStatement statement;
	private static ResultSet resSet;
	
	private static String getToken() {
		String token = "";
		Random rand = new Random();
		
		int i = 1;
		while (i <= 8) {
			token += (char)rand.nextInt('A', 'Z' + 1);
			i++;
		}
		
		return token;
	}
	
	public static String sendToken(String to) throws Exception {
		String sql = "SELECT * FROM users WHERE email = ?";
		
		conn = DomMebeli.connect();
		statement = conn.prepareStatement(sql);
		
		statement.setString(1, to);
		resSet = statement.executeQuery();
		
		try {
			if (resSet.next()) {
				String from, host, port;
				
				from = "mailtest09072023@gmail.com";
				host = "smtp.gmail.com";
				port = "465";
				
				Properties properties = new Properties();
				properties.setProperty("mail.smtp.host", host);
				properties.setProperty("mail.smtp.port", port);
				properties.setProperty("mail.smtp.ssl.enable", "true");
				properties.setProperty("mail.smtp.auth", "true");
				
				Session session = Session.getInstance(
					properties,
					new Authenticator() {
						@Override
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(from, "jjcxvrhcvordqewz");
						}
					}
				);
				
				try {
					String token = getToken();
					MimeMessage msg = new MimeMessage(session);
					
					msg.setFrom(new InternetAddress(from));
					msg.addRecipient(RecipientType.TO, new InternetAddress(to));
					
					msg.setSubject("Отправка токена для сброса пароля");
					msg.setText(token);
					
					Transport.send(msg);
					
					return token;
				} catch (Exception ex) {
					throw ex;
				}
			} else {
				throw new EmailNotFound("Такого Эл.адреса не существует в базе данных!");
			}
		} catch (EmailNotFound ex) {
			throw ex;
		}
	}
}

class EmailNotFound extends Exception {
	EmailNotFound(String msg) {
		super(msg);
	}
}