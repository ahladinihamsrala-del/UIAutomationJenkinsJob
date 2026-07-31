package com.ixigo.travelbooking.hotels.pages;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.ixigo.travelbooking.driver.DriverManager;
import com.ixigo.travelbooking.pages.BasePage;

public class HotelsSearchPage extends BasePage {

	public HotelsSearchPage(WebDriver driver) {
		super(driver);
		
	}
	
	By hotelsDestinationTxt = By.xpath("//p[normalize-space()='Destination']/following::input[1]");
	By hotelsCheckinLinkTxt = By.xpath("//p[normalize-space()='Check-in']");
	By roomsIncrementIcon = By.xpath("//p[@data-testid='room-increment']");
	By adultsIncrementIcon = By.xpath("//p[@data-testid='adult-increment']");
	By childincrementIcon = By.xpath("//p[@data-testid='counter-increment-children']");
	
	By child1AgeSelector = By.xpath("(//select[@data-testid='child-age-selector'])[1]");
	By searchButton = By.xpath("//div[text()='Search']");
	

	public HotelsSearchResultsPage searchHotels(Map<String,String>userInfo )
	
	{
		elementsUtil.doClickJS(hotelsDestinationTxt);
		elementsUtil.doSendKeys(hotelsDestinationTxt, userInfo.get("City"));
		By searchLocation = By.xpath("//p[text()='"+userInfo.get("Location")+"']");
		elementsUtil.doClickWhenVisible(searchLocation);
		
		elementsUtil.doClickWhenVisible(hotelsCheckinLinkTxt);
		
		By checkInDate = By.xpath("//abbr[contains(@aria-label,'"+userInfo.get("Checkin date")+"')]");
		By checkOutDate = By.xpath("//abbr[contains(@aria-label,'"+userInfo.get("Checkout date")+"')]");
		
		elementsUtil.doClickJS(checkInDate);
		elementsUtil.doClickJS(checkOutDate);
		
		elementsUtil.doClickJS(adultsIncrementIcon);
		elementsUtil.doClickJS(childincrementIcon);
		elementsUtil.selectByVisibleText(child1AgeSelector,"8 years");
		elementsUtil.doClickJS(searchButton);
		
		return new HotelsSearchResultsPage(DriverManager.getDriver());
		
	}
}
