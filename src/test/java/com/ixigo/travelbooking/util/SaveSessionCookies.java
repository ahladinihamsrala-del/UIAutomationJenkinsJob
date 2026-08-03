package com.ixigo.travelbooking.util;



import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import com.ixigo.travelbooking.pages.HomePage;

public class SaveSessionCookies {

	public static void main(String[] args) throws Exception {
		
		ChromeOptions options = new ChromeOptions(); // NOT headless mode — we need to see and interact
		WebDriver driver = new ChromeDriver(options);
		PropertyFileReader prop = new PropertyFileReader();
		String siteURL = prop.getFromPropertyFile("url");
		System.out.print(siteURL);
		driver.get(siteURL);
		driver.manage().window().maximize();
		HomePage homepage = new HomePage(driver);
		homepage.openSignIn();
		homepage.loginFromHomePageMobile();
		System.out.println("=================================================");
		System.out.println("Log in manually now (enter mobile number + OTP).");
		System.out.println("Once fully logged in, come back here and press Enter.");
		System.out.println("=================================================");
		System.in.read();

		SessionCookieManager.saveCookies(driver);

		driver.quit();
	}
}