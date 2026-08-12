package com.ixigo.travelbooking.hotels.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.ixigo.travelbooking.pages.BasePage;

public class HotelAddCustomerDetailsPage extends BasePage {
//checking revert logic 
	public HotelAddCustomerDetailsPage(WebDriver driver) {
		super(driver);
		
	}
	
	By firstNameTxt = By.xpath("//input[@data-testid='first-name-input']");
	By lastNameTxt = By.xpath("//input[@data-testid='last-name-input']");
	By emailTxt = By.xpath("//input[@data-testid='email-input']");
	By mobileInputTxt = By.xpath("//input[@data-testid='mobile-input']");
	By payNowButton = By.xpath("//button[text()='Pay Now']");
	
	public void addCustomerDetails()
	{
		/*elementsUtil.doSendKeys(firstNameTxt, "James");
		elementsUtil.doSendKeys(lastNameTxt, "Tim");
		elementsUtil.doSendKeys(emailTxt, "ahladini.hamsrala@gmail.com");*/
		elementsUtil.scrollToElement(payNowButton);
		elementsUtil.doClickJSbyElementPresence(payNowButton);
		System.out.println("Added customer details");
		

		
	}
	

}
