package com.ixigo.travelbooking.buses.pages;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.ixigo.travelbooking.driver.DriverManager;
import com.ixigo.travelbooking.pages.BasePage;

public class BusResultsPage extends BasePage{

	public BusResultsPage(WebDriver driver) {
		super(driver);
		
	}

By bustypeAC= By.xpath("//span[text()='AC']");
By bustypesleeper= By.xpath("//span[text()='Sleeper']");
By bussubtitle= By.xpath("(//div[contains(@class,'container sub-title  ')])[1]");
By busdetailsLink= By.xpath("//a[text()='Bus details']");
By busdetailsClose= By.xpath("//a[contains(@class,'btn back-btn light text primary sm rounded-md inactive button')]//*[name()='svg']");
By busfirstSelect= By.xpath("(//button[text()='Select Seats'])[1]");

By boardingPointSearch= By.xpath("//input[@placeholder='Search Boarding Point']");
By droppingPointSearch= By.xpath("//input[@placeholder='Search Dropping Point']");
By selectOptions= By.xpath("(//div[@class='label'])[1]");
By seatAvailable= By.xpath("(//table[@id='seat-layout-details']//button[contains(@class,'seat') and .//span[normalize-space()!='']])[1]");
By proceedButton= By.xpath("//button[text()='Proceed']");


public String getSearchPageURL()
{
	return elementsUtil.getPageURL();
}

public String applyBusFilters()
{
	elementsUtil.doClick(bustypeAC);
	elementsUtil.doClick(bustypesleeper);
return	elementsUtil.getVisibleText(bussubtitle);
	
}
public PassengerInfoPage bookBus(Map<String, String> busInfo)
{
	elementsUtil.doClick(busdetailsLink);
	elementsUtil.doClickWhenVisible(busdetailsClose);
	elementsUtil.doClickWhenVisible(busfirstSelect);
	elementsUtil.doSendKeys(boardingPointSearch, busInfo.get("Boarding point"));
	//elementsUtil.doClickWhenVisible(selectOptions);
	elementsUtil.doClickJSbyElementPresence(selectOptions);
	//elementsUtil.doSendKeys(droppingPointSearch, busInfo.get("Dropping point"));
	elementsUtil.doClickJSbyElementPresence(selectOptions);
	elementsUtil.scrollToElement(seatAvailable);
	elementsUtil.doClickJS(seatAvailable);
	elementsUtil.doClickJSbyElementPresence(proceedButton);
	
	return new PassengerInfoPage(DriverManager.getDriver());
	
	
	

}
}
