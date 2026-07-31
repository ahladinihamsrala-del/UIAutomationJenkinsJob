package com.ixigo.travelbooking.pages;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import com.ixigo.travelbooking.driver.DriverManager;

public class TravellerInfoPage extends BasePage {

	public TravellerInfoPage(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}
	

	By travelCtnButton =By.xpath("//button[@data-testid='travellers_continue_btn']");
	By reviewTripCtnButton =By.xpath("//button[@data-testid='reviewtrip_continue_btn']");
	By revDeptLoc=By.xpath("(//div[contains(@class,'py-20')]//h5[contains(@class,'text-primary')]/span[1])[1]");
	By revTravelDate=By.xpath("(//div[contains(@class, 'p-4')])[2]/div[1]/p[1]");
	By revTravelTiming=By.xpath("(//div[contains(@class, 'p-4')])[2]/div[1]/p[3]");
	By revTravelFare=By.xpath("(//div[contains(@class, 'p-4')])[2]//div[contains(@class, 'dreNhU')]/p");
	By contactEmailtxt=By.xpath("//input[@data-testid='contact_email']");
	By chooseCoupon=By.xpath("(//input[@type='radio'])[2]");
	By revDestinationLoc=By.xpath("(//div[contains(@class,'py-20')]//h5[contains(@class,'text-primary')]/span[2])[1]");
	
	By adult1TitleTxt=By.xpath("//div[@id='adult1']//label[normalize-space()='Title']/following::input[1]");
	By adult1Titleslct=By.xpath("//p[text()='Mr']");
	By adult1FirstNameTxt=By.xpath("//div[@id='adult1']//label[normalize-space()='First & Middle Name']/following::input[1]");
	
	By adult1LastNameTxt=By.xpath("//div[@id='adult1']//label[normalize-space()='Last Name']/following::input[1]");		
	By continueButton=By.xpath("//button[text()='Continue']");
	By freeCancelOption =By.xpath("//p[text()='Free Cancellation']");
	By confirmButton=By.xpath("//button[text()='Confirm']");
	By skipButton=By.xpath("//button[text()='Skip to Payment']");
	By childCheckBox=By.xpath("(//input[@type='checkbox'])[2]");
	By adult1CheckBox=By.xpath("(//input[@type='checkbox'])[1]");
	By emailBox=By.xpath("//label[normalize-space()='Email']/following::input[1]");
	
	public PaymentPage addTravellerInfo(Map<String, String> userInfoRepo)
	{
		elementsUtil.doScrollToTop();
		elementsUtil.scrollToElement(freeCancelOption);
		elementsUtil.doClickJSbyElementPresence(freeCancelOption);
		
		/*elementsUtil.scrollToElement(adult1TitleTxt);
		elementsUtil.doClickJSbyElementPresence(adult1TitleTxt);
		elementsUtil.doClickJSbyElementPresence(adult1Titleslct);
		elementsUtil.doSendKeys(adult1FirstNameTxt, userInfoRepo.get("First name"));
		elementsUtil.doSendKeys(adult1LastNameTxt, userInfoRepo.get("Last name"));*/
		elementsUtil.scrollToElement(adult1CheckBox);
		//elementsUtil.doClickJSbyElementPresence(adult1CheckBox);
		
		elementsUtil.doClickWhenVisible(adult1CheckBox);
		
		elementsUtil.scrollToElement(childCheckBox);
		elementsUtil.doClickJSbyElementPresence(childCheckBox);
		//elementsUtil.scrollToElement(emailBox);
		//elementsUtil.doSendKeys(emailBox,userInfoRepo.get("Email address"));
		elementsUtil.scrollToElement(continueButton);
		elementsUtil.doClickJSbyElementPresence(continueButton);
		elementsUtil.doClick(confirmButton);
		
		elementsUtil.doClickWhenVisible(skipButton);
		
		
		
		ExtentCucumberAdapter.addTestStepLog(
	            "Traveller Information added");
		return new PaymentPage(DriverManager.getDriver());
	}
	
	//Locations review
	public String reviewDeptLoc()
	{
		
		
		return elementsUtil.getVisibleText(revDeptLoc);
		
		
	}
	
	public String reviewDestLoc()
	{
		return elementsUtil.getVisibleText(revDestinationLoc);
		
		
	}
	//DAte
	public String reviewTravelDate()
	{
		
		return elementsUtil.getVisibleText(revTravelDate);
		
	}
	public String reviewDeptTime()
	{
		return elementsUtil.getVisibleText(revTravelTiming);
		
	}
	
	public PaymentPage navigateToPaymentPage()
	{
		

		elementsUtil.doClickJS(reviewTripCtnButton);
		ExtentCucumberAdapter.addTestStepLog(
	            "Navigating to Payments page ");
		return new PaymentPage(DriverManager.getDriver());
		
		
	}
		
		
	public String verifyReviewPageLoaded()
	{
		return elementsUtil.getPageTitle();
	}
		
		
	public void verifyFlightdetailsReviewPage()
	{
		//choose coupon 
		elementsUtil.doClickJSbyElementPresence(chooseCoupon);
		
	}
		
		
		
		
		
		
		
		
		
		
		
		
	}

