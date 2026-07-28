package com.ixigo.travelbooking.hooks;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.ixigo.travelbooking.driver.BrowserContext;
import com.ixigo.travelbooking.driver.DriverFactory;
import com.ixigo.travelbooking.driver.DriverManager;
import com.ixigo.travelbooking.util.ElementsUtil;
import com.ixigo.travelbooking.util.PropertyFileReader;

import io.cucumber.java.AfterStep;
import io.cucumber.java.Scenario;

import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {

	static PropertyFileReader propreader = new PropertyFileReader();

	@Before(order = 0)
	public void setUp(Scenario scenario) {
		String browser = BrowserContext.getBrowser();
		if (scenario.getSourceTagNames().contains("@web")) {
	        ExtentCucumberAdapter.addTestStepLog(
	            MarkupHelper.createLabel(
	                "Browser: " + browser.toUpperCase(),
	                browser.equalsIgnoreCase("chrome") ? ExtentColor.GREEN : ExtentColor.ORANGE
	            ).getMarkup()
	        );
	    }
	
		WebDriver driver = DriverFactory.createDriver(browser);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		DriverManager.setDriver(driver);
	}

	@Before(order = 1)
	public void launchApplication() throws IOException {
		ElementsUtil elementsutil = new ElementsUtil(DriverManager.getDriver());
		ExtentCucumberAdapter.addTestStepLog(
                "Starting scenario:Launching URL " + propreader.getFromPropertyFile("url"));
		elementsutil.openURL(propreader.getFromPropertyFile("url"));
		

	}
	 @After(order=2)
	    public void takeScreenshot(Scenario scenario) {
		 	 

		 byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
	        scenario.attach(screenshot, "image/png", "Step Screenshot");
	    }
	

	@After(order=1)
	public void tearDown() {
		DriverManager.quitDriver();
	}
	}

