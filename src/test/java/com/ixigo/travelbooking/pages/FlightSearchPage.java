package com.ixigo.travelbooking.pages;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import com.ixigo.travelbooking.context.TravelInfoContext;
import com.ixigo.travelbooking.driver.DriverManager;
import com.ixigo.travelbooking.util.getDateFields;


public class FlightSearchPage extends BasePage {

	public FlightSearchPage(WebDriver driver) {
		super(driver);
	}

	/*
	 * Demo purpose
	 */
	
	By roundTripButton = By.xpath("//button[normalize-space()='Round Trip']");
	By fromCity = By.xpath("//span[normalize-space()='From']");
	By fromCityLabel = By.xpath("//p[normalize-space()='From']");
	By fromInputBox=By.xpath("//label[normalize-space()='From']/following-sibling::input[1]");
	By toCity = By.xpath("//span[normalize-space()='To']");

	By fromCityDropdown = By.xpath("(//input[contains(@class,'outline')])[1]");
	By toCityDropdown = By.xpath("(//input[contains(@class,'outline')])[2]");
	By tripTypeDropdown = By.xpath("//*[normalize-space()='Round Trip']/ancestor::*[self::label or self::div][1]");
	By departureDateField = By.xpath("//p[text()='Departure']");
	By returnDateField = By.xpath("//p[text()='Return']");
	
	//abbr[contains(@aria-label,'August 20, 2026')]

	By travelsClassDropdown = By.xpath("//div[text()='Travellers & Class']");
	By businessClassOption = By.xpath("//span[text()='Business']");
	By premiumClassOption = By.xpath("//span[text()='Premium Economy']");

	By addAdultButton = By.xpath("//div[text()='Adults']/ancestor::div[2]//div[@style='cursor: pointer;'][2]");

	By addChildButton = By
			.xpath("(//button[@data-testid='1'])[2]");

	By doneButton = By.xpath("//button[text()='Done']");
	By searchFlightsButton = By.xpath("//button[text()='Search']");
	
	TravelInfoContext travelInfoContext = new TravelInfoContext();
	

	By recommendedFilterchx = By.xpath("(//input[@type='checkbox' and @value='6E'])[2])");
	

public SearchResultsPage flightSearch(Map<String, String> userInfo)
{
	By searchresultFrom = By.xpath("//p[contains(text(),'" + userInfo.get("From location airport") + "')]");

	By searchresultTo = By.xpath("//p[contains(text(),'" + userInfo.get("To location airport") + "')]");
	
	By departureDate = By.xpath("//abbr[contains(@aria-label,'"+userInfo.get("Departure date")+"')]");
	By returnDate = By.xpath("//abbr[contains(@aria-label,'"+userInfo.get("Return Date")+"')]");
	
	
	elementsUtil.doClickJS(roundTripButton); 
	//elementsUtil.doClick(fromCity); 
	elementsUtil.doClick(fromCityLabel); 
	elementsUtil.doClick(fromInputBox); 
	elementsUtil.clearAllText(fromInputBox);
	elementsUtil.doSendKeys(fromInputBox, userInfo.get("From location"));
	elementsUtil.doClick(searchresultFrom);
	elementsUtil.doClickJS(toCity);
	elementsUtil.doSendKeys(toCityDropdown, userInfo.get("To location"));
	elementsUtil.doClickJS(searchresultTo);
	
	elementsUtil.doClick(departureDateField);
	elementsUtil.doClick(departureDate);
	//elementsUtil.doClick(returnDateField);
	elementsUtil.doClick(returnDate);
	
	elementsUtil.doClick(addChildButton);
	
	// Choosing the Travel class
			if (userInfo.get("Travel Class").equals("Business"))
				elementsUtil.doClick(businessClassOption);
			else if (userInfo.get("Travel Class").equals("Premium Economy"))
				elementsUtil.doClick(premiumClassOption);
	
	//span[text()='Economy']
	elementsUtil.doClick(doneButton);
	elementsUtil.doClickJS(searchFlightsButton);
	
	
	return new SearchResultsPage(DriverManager.getDriver());
}

public void validateRecommenedFilters()

{
	elementsUtil.doClickJSbyElementPresence(recommendedFilterchx);
	System.out.println(elementsUtil.getVisibleText(recommendedFilterchx));
}

}
