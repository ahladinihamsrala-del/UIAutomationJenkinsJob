package com.ixigo.travelbooking.pages;




import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import com.ixigo.travelbooking.buses.pages.BusesSearchPage;
import com.ixigo.travelbooking.driver.DriverManager;
import com.ixigo.travelbooking.hotels.pages.HotelsSearchPage;
import com.ixigo.travelbooking.util.PropertyFileReader;
import com.ixigo.travelbooking.util.ScreenshotUtil;

import io.cucumber.java.Scenario;


public class HomePage extends BasePage {

public HomePage(WebDriver driver) {
super(driver);
}

//By Locators for UI element handles
private By signInIcon = By.xpath("(//button[text()='Log in/Sign up'])[1]");
By mobileNumberInput = By.xpath("//input[@placeholder='Enter Mobile Number']");
By loginContinueButton = By.xpath("//button[text()='Continue']");
By signInLink = By.xpath("//div[@id='googleLogin']");
By emailInput = By.xpath("//input[@aria-label='Email or phone']");
By pwdInput = By.xpath("//input[@name='Passwd']");
By nextButton = By.xpath("//span[normalize-space()='Next']");

By popCloseButton = By.xpath("//*[@id=\"closeButton\"]/span");
By loginText = By.xpath("(//span[text()='Hey'])[1]");

//Hotels
By hotelsLink = By.xpath("(//p[text()='Hotels'])[2]");
By busesLink = By.xpath("(//p[text()='Buses'])[2]");

By popupCloseButton = By.id("closeButton");




PropertyFileReader propReader=new PropertyFileReader();

public String verifyHomePage()
{
	
			
	return elementsUtil.getPageURL();
}
public void openSignIn() throws IOException
{
/*	WebElement popupElement = driver.findElement(popCloseButton);
	if(popupElement.isDisplayed())
		popupElement.click();*/
	
	elementsUtil.doClickJS(signInIcon);
	//elementsUtil.doSendKeys(mobileNumberInput,prop.getFromPropertyFile("Mobile") );
}

public void loginFromHomePageMobile() throws InterruptedException, IOException
{
	
	elementsUtil.doSendKeys(mobileNumberInput, propReader.getFromPropertyFile("Mobile"));
	
	elementsUtil.doClickJS(loginContinueButton);
	Thread.sleep(30000);//explicit delay to enter the OTP 
	ExtentCucumberAdapter.addTestStepLog(
            "Login successful now navigating to ixigo page ");
	elementsUtil.pageRefresh();
}

public void loginFromHomePageGmail() throws InterruptedException, IOException
{
	
	//elementsUtil.doClickJSbyElementPresence(signInLink);
	//elementsUtil.doClickJSbyElementPresence(signInLink);
	//GoogleLoginClick.clickGoogleLogin(driver);
	elementsUtil.doSendKeys(emailInput, propReader.getFromPropertyFile("gmailusername"));
	elementsUtil.doClick(nextButton);
	elementsUtil.doSendKeys(pwdInput,propReader.getFromPropertyFile("gmailpassword"));
	elementsUtil.doClick(nextButton);
	Thread.sleep(30000);//explicit delay to accept notification on Mobile
	

}

public String verifyLoginText()
{

	return elementsUtil.getVisibleText(loginText);
	
}

public HotelsSearchPage navigateToHotelsSearch()
{
	
	
	elementsUtil.doClickJSbyElementPresence(hotelsLink);
	elementsUtil.dismissPopupIfPresent(By.id("closeButton"), 3);
	
	return new HotelsSearchPage(DriverManager.getDriver());

}

public BusesSearchPage navigateToBusesSearch()
{
	elementsUtil.doClickJSbyElementPresence(busesLink);
	elementsUtil.dismissPopupIfPresent(By.id("closeButton"), 3);
	
	return new BusesSearchPage(DriverManager.getDriver());

}
}
