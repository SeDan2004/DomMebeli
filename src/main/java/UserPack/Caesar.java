package UserPack;

public class Caesar {
	private static int offset = 186;
	
	public static String caesar(String str, String type) {
		String res = "";
		
		for (int i = 0; i < str.length(); i++) {
			res += type == "decrypt" ? (char)(str.charAt(i) - offset) :
				                       (char)(str.charAt(i) + offset);
		}
		
		return res;
	}
}