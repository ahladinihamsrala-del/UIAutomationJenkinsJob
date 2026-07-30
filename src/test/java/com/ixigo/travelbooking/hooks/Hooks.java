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
import com.ixigo.travelbooking.util.SessionCookieManager;

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
	public void launchApplication(Scenario scenario) throws IOException {
		ElementsUtil elementsutil = new ElementsUtil(DriverManager.getDriver());
		String url = propreader.getFromPropertyFile("url");

		ExtentCucumberAdapter.addTestStepLog("Starting scenario:Launching URL " + url);
		elementsutil.openURL(url);

		if (scenario.getSourceTagNames().contains("@loggedIn")) {
			try {
				SessionCookieManager.loadCookies(DriverManager.getDriver(), url);

				boolean sessionValid = elementsutil.isElementVisible(
						org.openqa.selenium.By.xpath("(//span[text()='Hey'])[1]"), 8
				);

				if (!sessionValid) {
					ExtentCucumberAdapter.addTestStepLog(
							MarkupHelper.createLabel(
									"SESSION EXPIRED: Saved cookies did not authenticate. "
									+ "Regenerate session-cookies.ser locally (run SaveSessionCookies) "
									+ "and re-upload it to the 'ixigo-session-cookies' Jenkins credential.",
									ExtentColor.RED
							).getMarkup()
					);
					throw new RuntimeException("Session expired - cookies did not authenticate");
				}

				ExtentCucumberAdapter.addTestStepLog(
						MarkupHelper.createLabel("Session verified - logged in successfully", ExtentColor.GREEN)
								.getMarkup()
				);

			} catch (Exception e) {
				ExtentCucumberAdapter.addTestStepLog(
						MarkupHelper.createLabel(
								"Failed to load/verify session cookies: " + e.getMessage(),
								ExtentColor.RED
						).getMarkup()
				);
				throw new IOException("Session cookie load/verification failed", e);
			}
		}
	}
	 @AfterStep
	    public void takeScreenshot(Scenario scenario) {
		 	 

		 byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
	        scenario.attach(screenshot, "image/png", "Step Screenshot");
	    }
	

	@After
	public void tearDown() {
		DriverManager.quitDriver();
	}
	}

