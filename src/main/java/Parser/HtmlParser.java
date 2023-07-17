package Parser;

import static Database.DomMebeli.conn;
import static Database.DomMebeli.resSet;
import static Database.DomMebeli.statement;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import Database.DomMebeli;

public class HtmlParser {
	
	private static String url;
	private static String webappDir;
	private static WebDriver driver;
	private static ChromeOptions options;
	private static JavascriptExecutor jse;
	
	private static List<String> categories;
	private static List<List<String[]>> items;
	
	static {
		url = "https://bestmebelshop.ru";
		webappDir = System.getProperty("user.dir") + "\\src\\main\\webapp";
		items = new ArrayList<>();
	}
	
	private static WebDriver driverInit() {
		System.setProperty("webdriver.chrome.driver", "C:/ChromeDriverWin/chromedriver.exe");
		
		options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		
		return new ChromeDriver(options);
	}
	
	private static void setProductsInDb() {
		try {
			conn = DomMebeli.connect();
			
			String sql;
			String category;
			StringBuilder imagesDir = new StringBuilder("\\Images");
			
			long categoryId;
			
			int itemCount = 1;
			
			int i = 0;
			while (i < categories.size()) {
				category = categories.get(i);
				
				sql = "INSERT INTO product_type (type) VALUES ('%s')";
				sql = String.format(sql, category);
				
				statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				statement.execute();
				
				resSet = statement.getGeneratedKeys();
				resSet.next();
				
				categoryId = resSet.getLong(1);
				
				for (int j = 0; j < items.get(i).size(); j++) {
					String[] item;
					String name, imgSrc, price, format;
					StringBuilder priceSb;
					
					item = items.get(i).get(j);
					
					name = item[0];
					
					price = item[1];
					price = price.substring(0, price.lastIndexOf(" "));
					
					priceSb = new StringBuilder(price);
					
					while (priceSb.indexOf(" ") != -1) {
						int pos = priceSb.indexOf(" ");
						priceSb.replace(pos, pos + 1, "");
					}
					
					price = priceSb.toString();
					
					imgSrc = item[2];
					format = imgSrc.substring(imgSrc.lastIndexOf('.') + 1);
					
					sql = "INSERT INTO product (type_id, name, price, img_src) " +
					      "VALUES (?, ?, ?, ?)";
					
					statement = conn.prepareStatement(sql);
					
					imagesDir.append("\\mebel" + itemCount + "." + format);
					
					BufferedImage bimg = ImageIO.read(new URL(imgSrc));
					File img = new File(webappDir + imagesDir);
					ImageIO.write(bimg, format, img);
					
					while (imagesDir.indexOf("\\") != -1) {
						int pos = imagesDir.indexOf("\\");
						imagesDir.replace(pos, pos + 1, "/");
					}
					
					statement.setInt(1, (int)categoryId);
					statement.setString(2, name);
					statement.setDouble(3, Double.parseDouble(price));
					statement.setString(4, imagesDir.toString());
					
					statement.execute();
					
					imagesDir = new StringBuilder("\\Images");
					itemCount++;
				}
				
				i++;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private static List<String> getCategories(List<WebElement> sectInner) {
		return sectInner.stream()
				        .map((el) -> {
				        	return el.findElement(By.cssSelector(".name"))
				        			 .getText();
				        }).toList();
	}
	
	private static List<String> getPageLinks(List<WebElement> sectInner) {
		return sectInner.stream()
				        .map((el) -> {
				        	return el.findElement(By.cssSelector("a"))
				        			 .getAttribute("href");
				        }).toList();
	}
	
	private static void getProducts() {
		try {
			driver = driverInit();
			jse = (JavascriptExecutor)driver;
			
			driver.get(url);
			
			List<WebElement> sectInner;
			List<String> pagesLink;
			
			sectInner = driver.findElements(By.cssSelector(".sect_inner"))
			                  .subList(0, 5);
			
			categories = getCategories(sectInner);
			pagesLink = getPageLinks(sectInner);
			
			for (int i = 0; i < pagesLink.size(); i++) {
				driver.get(pagesLink.get(i));
				items.add(new ArrayList<>());
				
				List<WebElement> products;
								
				products = driver.findElements(By.cssSelector(".alow_width_change"))
				          		 .subList(0, 20);
				
				for (WebElement product : products) {
					String imgSrc, price, name;
					
					imgSrc = product.findElement(By.cssSelector(".seredina img"))
							        .getAttribute("src");
					
					if (imgSrc.endsWith(".png")) {
						String src = product.findElement(By.cssSelector(".seredina noscript"))
										    .getAttribute("innerText");
						
						src = src.substring(src.indexOf("\"") + 1, src.indexOf("\" alt"));
						imgSrc = url + src;
					}
					
					price = product.findElement(By.cssSelector(".catalog-price"))
							       .getText();
							      
					name = product.findElement(By.cssSelector(".nazvanie"))
							      .getText();
					
					items.get(i).add(new String[] {name, price, imgSrc});
				}	
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		getProducts();
		setProductsInDb();
	}
}