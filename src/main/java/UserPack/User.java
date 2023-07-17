package UserPack;

import static Database.DomMebeli.conn;
import static Database.DomMebeli.resSet;
import static Database.DomMebeli.statement;
import static Database.DomMebeli.writer;

import java.sql.Statement;

import Database.DomMebeli;

public class User {
	private String id;
	private String login;
	private String password;
		
	private Backet[] backets;
	
	User(String id, String login, String password) {
		this.id = id;
		this.login = login;
		this.password = password;
	}
	
	public static User addUser(String userLogin, String userPass, String userEmail) 
	throws Exception {
		try {
			String id = checkUser(userLogin, userPass);
		
			if (id == null) {
				String sql = "INSERT INTO users (login, email, password) " + 
			                 "VALUES (?, ?, ?)";
				
				conn = DomMebeli.connect();
				statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				
				statement.setString(1, userLogin);
				statement.setString(2, userEmail);
				statement.setString(3, userPass);
				
				statement.execute();
				
				resSet = statement.getGeneratedKeys();
				resSet.next();
				
				return new User(resSet.getLong(1) + "", userLogin, userPass);
			} else {
				throw new UserExist("Такой пользователь уже существует!");
			}
		} catch (UserExist ex) {
			writer.println(ex.getMessage());
			return null;
		}
	}
	
	public static String checkUser(String userLogin, String userPass) 
	throws Exception {
		String sql = "SELECT id FROM users WHERE login = ? AND password = ?";
		
		conn = DomMebeli.connect();
		statement = conn.prepareStatement(sql);
		
		statement.setString(1, userLogin);
		statement.setString(2, userPass);
		
		resSet = statement.executeQuery();
		
		return resSet.next() ? resSet.getString("id") : null;
	}
	
	public static User checkUserInDb(String userLogin, String userPass)
	throws Exception {
		try {
			String id = checkUser(userLogin, userPass);
		
			if (id != null) {
				return new User(id, userLogin, userPass);
			} else {
				throw new WrongLoginOrPassword("Указан неверный логин или пароль!");
			}
		} catch (WrongLoginOrPassword ex) {
			writer.println(ex.getMessage());
			return null;
		}
	}
		
	public static void updateUserPass(String newPass, String email) throws Exception {
		String sql;
		
		sql = "UPDATE users SET password = ? WHERE email = ?";
		conn = DomMebeli.connect();
		statement = conn.prepareStatement(sql);
		
		statement.setString(1, newPass);
		statement.setString(2, email);
		
		statement.execute();
	}
	
	public String getId() {
		return id;
	}
	
	public String getLogin() {
		return login;
	}
	
	public String getPassword() {
		return password;
	}
}

class WrongLoginOrPassword extends Exception {
	WrongLoginOrPassword(String msg) {
		super(msg);
	}
}

class UserExist extends Exception {
	UserExist(String msg) {
		super(msg);
	}
}