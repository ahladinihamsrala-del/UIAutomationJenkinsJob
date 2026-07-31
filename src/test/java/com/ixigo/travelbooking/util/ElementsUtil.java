package com.ixigo.travelbooking.util;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ElementsUtil {

	private final WebDriver driver;
	private final WebDriverWait wait;

	public ElementsUtil(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(60));// adding a timeout of 40 seconds
	}

	// This method is for the explicit wait for the element to be visible
	public WebElement waitForVisibility(By locator) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
	// This method is for the explicit wait for the element to be visible
		public WebElement presenceOfElement(By locator) {
			return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		}

	// This method is for the explicit wait for the element to be clickable
	public WebElement waitForClickability(By locator) {
		return wait.until(ExpectedConditions.elementToBeClickable(locator));
	}

	// This method is for the explicit wait to check if the URL contains given
	// string
	public void waitForUrlContains(String partialUrl) {
		wait.until(ExpectedConditions.urlContains(partialUrl));
	}

	// Wait for page load completion
	public void waitForPageToLoad() {
		wait.until(
				driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
	}

	public void selectByVisibleText(By locator, String visibleText) {
		Select select = new Select(waitForVisibility(locator));
		select.selectByVisibleText(visibleText);
	}

	public void selectByValue(By locator, String value) {
		Select select = new Select(waitForVisibility(locator));
		select.selectByValue(value);
	}

	public void selectByIndex(By locator, int index) {
		Select select = new Select(waitForVisibility(locator));
		select.selectByIndex(index);
	}

	public void scrollToElement(By locator) {
		WebElement element = presenceOfElement(locator);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public void doSendKeys(By locator, String value) {
		WebElement element = waitForVisibility(locator);
		//element.clear();
		element.sendKeys(value);
	}

	public void doClick(By locator) {
		WebElement element = waitForClickability(locator);
		element.click();
	}

	public void doClickJS(By locator) {
		WebElement element = waitForClickability(locator);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", element);

	}

	public String getVisibleText(By locator) {
		WebElement element = waitForVisibility(locator);
		return element.getText();
	}
		public String getPresenceText(By locator) {
			waitForPageToLoad();
			WebElement element = presenceOfElement(locator);
			return element.getText();

	}

	public String getPageTitle() {
		//waitForUrlContains("result/flight");
		waitForPageToLoad();
		return driver.getTitle();
	}
	
	public String getPageURL() {
		waitForPageToLoad();
		return driver.getCurrentUrl();
	
	}

	public void openURL(String url) {
		driver.get(url);
		driver.manage().window().maximize();
	}
	public void doClickWhenVisible(By locator) {
	    WebElement element = waitForVisibility(locator);
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
	    element.click();
	}
	
	public void doClickJSbyElementPresence(By locator) {
        WebElement element = presenceOfElement(locator);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
        js.executeScript("arguments[0].click();", element);
        //System.out.println("Button clicked");
    }
	
	public int getNumberofElementsMatched(By locator)
	
	{
		List<WebElement> elements = wait.until(
			    ExpectedConditions.presenceOfAllElementsLocatedBy(locator)
			);
	List<WebElement>elementsfound=driver.findElements(locator);
	return elementsfound.size();
	}
	
	public void pageRefresh()
	{
		driver.navigate().refresh();
	}
	public void doScrollToTop() {
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, 0);");//scroll to Top of the page
}
	
	public void clearText(By locator)
	{
		WebElement element = waitForVisibility(locator);
		driver.findElement(locator).clear();
	}
	
	public void clearAllText(By locator)
	{
		WebElement element = waitForVisibility(locator);
		element.click();
		element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
		element.sendKeys(Keys.BACK_SPACE);
	}
	public boolean isElementVisible(By locator, int timeoutSeconds) {
	    try {
	        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
	        shortWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	        return true;
	    } catch (org.openqa.selenium.TimeoutException e) {
	        return false;
	    }
	}
	
	public void safeClick(WebDriver driver, By locator) {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));

	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    js.executeScript("arguments[0].scrollIntoView({block:'center'});", el);

	    try {
	        el.click();
	    } catch (org.openqa.selenium.ElementClickInterceptedException e) {
	        // fallback to JS click if something is still overlapping
	        js.executeScript("arguments[0].click();", el);
	    }
	}
	//safeClick(driver, By.xpath("//button[@id='submit']"));
	
	public void dismissPopupIfPresent(By closeButtonLocator, int timeoutSeconds) {
	    try {
	        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
	        WebElement closeBtn = shortWait.until(ExpectedConditions.elementToBeClickable(closeButtonLocator));
	        closeBtn.click();
	        System.out.println("Dismissed a promotional popup.");
	    } catch (org.openqa.selenium.TimeoutException e) {
	        System.out.println("No popup appeared - continuing.");
	    }
	}
}


