package com.ixigo.travelbooking.hotels.pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.ixigo.travelbooking.driver.DriverManager;
import com.ixigo.travelbooking.pages.BasePage;
import com.ixigo.travelbooking.hotels.pages.HotelRoomDetailsPage;
import com.ixigo.travelbooking.util.WindowUtils;

public class HotelsSearchResultsPage extends BasePage {

	public HotelsSearchResultsPage(WebDriver driver) {
		super(driver);

	}

	By sortOption = By.xpath("//p[contains(text(),'Sort by')]");
	By priceLowToHighOption = By.xpath("//p[contains(text(),'Low to High')]");
	By pricesDispTxt = By.xpath("//div[contains(text(),'₹') and contains(@class,'text-primary')]");
	// By
	// bestPriceToggleSwitch=By.xpath("//span[@data-testid='bpg-toggle-switch']");
	By bestPriceToggleSwitch = By.xpath("//div[@data-testid='bpg-toggle-container']//input[@type='checkbox']");
	By closePopUpButton = By.xpath("//div[@aria-label='Close modal']");
	By exceptionalRatingOption = By.xpath("//p[contains(text(),'Rated Exceptional')]");
	By exceptionalResultsTag = By.xpath("(//p[text()='Exceptional'])[1]");
	By localitySearchTxt = By.xpath("//input[contains(@placeholder,'Enter area')]");

	By internetFacilityFilter = By.xpath("//p[text()='Internet access']");
	By freeWifiTag = By.xpath("//p[@data-testid='offering' and normalize-space()='Free Wifi']");

	By BestPriceGuaranteeTag = By.xpath("//img[@data-testid='bpg-chip-image']");
	By bestPriceGuaranteeHotelBookButton = By.xpath("(//button[text()='Book Now'])[1]");

	public String getPageURL() {
		String currentURL = elementsUtil.getPageURL();

		return currentURL;
	}

	public List<Double> getListedPrices() {
		elementsUtil.doClickJSbyElementPresence(sortOption);
		elementsUtil.doClickJSbyElementPresence(priceLowToHighOption);

		List<Double> actualPrices = driver.findElements(pricesDispTxt).stream().map(WebElement::getText)
				.map(price -> price.replaceAll("[^0-9.]", "")).map(Double::parseDouble).collect(Collectors.toList());
		return actualPrices;
	}

	public List<Double> SortListedPrices(List<Double> actualPrices) {
		List<Double> expectedPrices = new ArrayList<>(actualPrices);
		Collections.sort(expectedPrices);
		return expectedPrices;

	}

	public String verifyFilters()

	{
		if (driver.findElement(closePopUpButton).isDisplayed())
			elementsUtil.doClickJS(closePopUpButton);
		elementsUtil.scrollToElement(exceptionalRatingOption);
		elementsUtil.doClickJS(exceptionalRatingOption);
		return elementsUtil.getVisibleText(exceptionalResultsTag);
	}

	public boolean verifyLocalSearch(String Local) {
		
		
		
		By localSearchTxt = By.xpath("//div[@data-testid='" + Local + "']");

		

		elementsUtil.scrollToElement(localitySearchTxt);

		elementsUtil.doSendKeys(localitySearchTxt, Local);
		elementsUtil.pageRefresh();
		
		//elementsUtil.clearText(localitySearchTxt);
		elementsUtil.scrollToElement(localitySearchTxt);
		elementsUtil.doSendKeys(localitySearchTxt, Local);
		

		//elementsUtil.doClickJSbyElementPresence(localSearchTxt);

		if (elementsUtil.getPageURL().contains(Local))
			return true;
		else
			return false;
	}

	public boolean verifyFacilityFilters() {
		elementsUtil.scrollToElement(internetFacilityFilter);
		elementsUtil.doClickJSbyElementPresence(internetFacilityFilter);

		int noOfElements = elementsUtil.getNumberofElementsMatched(freeWifiTag);
		System.out.println("noOfElements" + noOfElements);
		if (noOfElements > 0)
			return true;
		else
			return false;
	}

	public boolean verifyBestPriceGuaranteeFilter() {
		elementsUtil.scrollToElement(bestPriceToggleSwitch);
		elementsUtil.doClickJSbyElementPresence(bestPriceToggleSwitch);
		//elementsUtil.doClickWhenVisible(bestPriceToggleSwitch);
		elementsUtil.pageRefresh();

		int noOfElements = elementsUtil.getNumberofElementsMatched(BestPriceGuaranteeTag);
		System.out.println("noOfElements" + noOfElements);
		if (noOfElements > 0)
			return true;
		else
			return false;
	}

	public HotelRoomDetailsPage bookHotel() {
		// elementsUtil.doClickJSbyElementPresence(bestPriceGuaranteeHotelBookButton);
		String parentHandle = driver.getWindowHandle();

		String newTabHandle = WindowUtils.switchToNewTabAfterClick(driver,
				By.xpath("//button[normalize-space()='Book Now']"), Duration.ofSeconds(10));

		System.out.println("Parent: " + parentHandle);
		System.out.println("New Tab: " + newTabHandle);
		System.out.println("Title: " + driver.getTitle());

		return new HotelRoomDetailsPage(DriverManager.getDriver());

	}

}
