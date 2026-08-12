package com.ixigo.travelbooking.buses.pages;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.ixigo.travelbooking.driver.DriverManager;
import com.ixigo.travelbooking.pages.BasePage;

public class PassengerInfoPage extends BasePage {

	public PassengerInfoPage(WebDriver driver) {
		super(driver);
		
	}
	By nameTxt= By.xpath("//input[@placeholder='Name']");
	By ageTxt= By.xpath("//input[@placeholder='Age']");
	By emailTxt= By.xpath("//input[@placeholder='Email ID']");
	
	By contToPayTxt= By.xpath("//a[contains(text(),'Continue to Pay')]");
	By maleBtn= By.xpath("//button[contains(text(),'Male')]");
	By tripSecureRBtn= By.xpath("//span[text()='Secure this booking only']");
	

	public BusPaymentsPage enterPassengerInfo(Map<String, String> busInfo)
	{
		elementsUtil.scrollToElement(nameTxt);
		elementsUtil.doSendKeys(emailTxt, busInfo.get("Email"));
		elementsUtil.doSendKeys(nameTxt, busInfo.get("Name"));
		elementsUtil.doSendKeys(ageTxt, busInfo.get("Age"));
		elementsUtil.doClickJSbyElementPresence(maleBtn);
		elementsUtil.doClickJSbyElementPresence(tripSecureRBtn);
		//elementsUtil.scrollToElement(contToPayTxt);
		elementsUtil.doScrollToTop();
		elementsUtil.doClick(contToPayTxt);
		//elementsUtil.doClickJS(contToPayTxt);
		//elementsUtil.doClickJSbyElementPresence(contToPayTxt);
		
		return new BusPaymentsPage(DriverManager.getDriver());
	}

}
