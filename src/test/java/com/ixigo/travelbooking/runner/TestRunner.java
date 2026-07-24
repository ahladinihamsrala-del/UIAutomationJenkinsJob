
package com.ixigo.travelbooking.runner;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.ixigo.travelbooking.driver.BrowserContext;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
       		features = {//"src/test/resources/features/featuretorun.feature",
       				"src/test/resources/features/buses.feature",
       				"src/test/resources/features/flights.feature",
       				"src/test/resources/features/hotels.feature",
       				//"src/test/resources/features/login.feature",
       		},
       		
    glue = {
        "com.ixigo.travelbooking.stepdefinitions",
        "com.ixigo.travelbooking.hooks"
    },
    plugin = {
        "pretty",
        "html:target/cucumber-report.html",
        "json:target/cucumber-reports/cucumber.json",
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
    },
    monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {

    @BeforeClass(alwaysRun = true)
    @Parameters({ "browser" })
    public void setUpRun(@Optional("chrome") String browser) {
        BrowserContext.setBrowser(browser);
        
    }
}
