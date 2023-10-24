package UserPack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Database.DomMebeli;

public class Backet {
	private int backetId;
	private boolean status;
	
	private static Connection conn;
	private static PreparedStatement statement;
	private static ResultSet resSet;
	
	Backet(int backetId, boolean status) {
		this.backetId = backetId;
		this.status = status;
	}
	
	public static List<Backet> getBackets(String userId) throws Exception {
		String sql = "SELECT id, status FROM backets";
		List<Backet> res = new ArrayList<>();
			
		conn = DomMebeli.connect();
		statement = conn.prepareStatement(sql);
		resSet = statement.executeQuery();
			
		while (resSet.next()) {
			int backetId;
			boolean status;
				
			backetId = resSet.getInt("id");
			status = resSet.getBoolean("status");
				
			res.add(new Backet(backetId, status));
		}
			
		return res;
	}
	
	public static Backet createBacket(String userId) throws Exception {
		String sql = "INSERT INTO backets (user_id, status) " + 
	                 "VALUES (?, ?)";
			
		conn = DomMebeli.connect();
		statement = conn.prepareStatement(sql, statement.RETURN_GENERATED_KEYS);
		
		statement.setString(1, userId);
		statement.setBoolean(2, true);
		
		statement.execute();
			
		resSet = statement.getGeneratedKeys();
		resSet.next();
			
		return new Backet(resSet.getInt(1), true);
		
	}

	public static String generateBacketKey() throws Exception {
		String key = "";
		Random rand = new Random();
		
		for (int i = 1; i <= 8; i++) {
			int charCode = rand.nextInt(0, 2);
			
			key += charCode == 0 ? (char)rand.nextInt('a', 'z' + 1) :
				               	   (char)rand.nextInt('0', '9' + 1);
		}
		
		return key;
	}
	
	public static Backet checkBacketKey(String backetKey) throws Exception {
		String sql = "SELECT * FROM backets WHERE user_id = ?";
		
		conn = DomMebeli.connect();
		statement = conn.prepareStatement(sql);
		
		statement.setString(1, backetKey);
		
		resSet = statement.executeQuery();
		
		if (!resSet.next()) {
			throw new BacketNotFound("Корзина с таким ключом не найдена!");
		} else {
			int backetId = resSet.getInt("id");
			boolean status = resSet.getBoolean("status");
		
			return new Backet(backetId, status);
		}
	}
	
	public static Backet getActiveStatusBacket(List<Backet> backets) throws Exception {
		return backets.stream()
				      .filter(backet -> backet.status == true)
				      .findFirst()
				      .get();
	}
	
	public void updateBacketId(String id, String backetKey) throws Exception {
		String sql = "UPDATE backets SET user_id = ? WHERE user_id = ?";
		statement = conn.prepareStatement(sql);
		
		statement.setString(1, id);
		statement.setString(2, backetKey);
		
		statement.execute();
		
		this.backetId = Integer.parseInt(id);
	}
		
	public void addProductInBacket(int productId) throws Exception {
		String sql = "INSERT INTO backet_products (backet_id, product_id, count) " + 
	                 "VALUES (?, ?, ?)";
			
		conn = DomMebeli.connect();
		statement = conn.prepareStatement(sql);
			
		statement.setInt(1, backetId);
		statement.setInt(2, productId);
		statement.setInt(3, 1);
			
		statement.execute();
	}

	public int getBacketId() {
		return backetId;
	}
	
	public boolean getBacketStatus() {
		return status;
	}
	
	public void setStatusBacket(boolean status) throws Exception {
		String sql = "UPDATE backets SET status = ? WHERE id = ? AND status = 1";
		
		conn = DomMebeli.connect();
		statement = conn.prepareStatement(sql);
		
		statement.setBoolean(1, status);
		statement.setInt(2, this.backetId);
		
		statement.execute();
		
		this.status = status;
	}
	
	/*public static void checkBacketKey(String backetKey) throws Exception {
		
	}
	
	public static void checkBacket(String userId, int productId, PrintWriter writer) throws Exception {
		try {
			String sql = "SELECT COUNT(*) FROM backets WHERE user_id = ?";
			
			conn = DomMebeli.connect();
			statement = conn.prepareStatement(sql);
			
			statement.setString(1, userId);
			
			resSet = statement.executeQuery();
			resSet.next();
			
			int backetCount = resSet.getInt(1);
			
			if (backetCount != 0) {
				sql = "SELECT id FROM backets WHERE user_id = ? AND status = 1";
				
				statement = conn.prepareStatement(sql);
				
				statement.setString(1, userId);
				
				resSet = statement.executeQuery();
				resSet.next();
				
				backetId = resSet.getInt(1);
				
				sql = "INSERT INTO backet_products (backet_id, product_id, count) " +
				      "VALUES (?, ?, ?)";
				
				statement = conn.prepareStatement(sql);
				
				statement.setInt(1, backetId);
				statement.setInt(2, productId);
				statement.setInt(3, 1);
				
				statement.execute();
			} else {
				sql = "INSERT INTO backets (user_id, status) VALUES (?, ?)";
				statement = conn.prepareStatement(sql, statement.RETURN_GENERATED_KEYS);
				
				statement.setString(1, userId);
				statement.setBoolean(2, true);
				
				statement.execute();
				
				resSet = statement.getGeneratedKeys();
				resSet.next();
				
				backetId = resSet.getInt(1);
				
				sql = "INSERT INTO backet_products (backet_id, product_id, count) " +
				      "VALUES (?, ?, ?)";
				
				statement = conn.prepareStatement(sql);
				
				statement.setInt(1, backetId);
				statement.setInt(2, productId);
				statement.setInt(3, 1);
				
				statement.execute();
			}
		} catch (Exception ex) {
			throw ex;
		}
	}*/
}

class BacketNotFound extends Exception {
	BacketNotFound(String msg) {
		super(msg);
	}
}