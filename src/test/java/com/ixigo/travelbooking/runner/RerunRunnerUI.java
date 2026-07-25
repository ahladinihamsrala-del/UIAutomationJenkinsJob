package com.ixigo.travelbooking.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.ixigo.travelbooking.driver.BrowserContext;

/**
 * Re-executes only the UI/Selenium scenarios that FAILED in the previous run.
 * Mirrors TestRunner.java's glue/browser setup - add "rerun:target/rerun-ui.txt"
 * to TestRunner's plugin list so Cucumber writes failures there (see README).
 *
 * Triggered from Jenkins only when target/rerun-ui.txt is non-empty.
 */
@CucumberOptions(
        features = "@target/rerun-ui.txt",
        glue = {
                "com.ixigo.travelbooking.stepdefinitions",
                "com.ixigo.travelbooking.hooks"
        },
        plugin = {
                "pretty",
                "html:target/cucumber-report-rerun-ui.html",
                "json:target/cucumber-report-rerun-ui.json",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        monochrome = true
)
public class RerunRunnerUI extends AbstractTestNGCucumberTests {

    @BeforeClass(alwaysRun = true)
    @Parameters({"browser"})
    public void setUpRun(@Optional("chrome") String browser) {
    	BrowserContext.setBrowser(browser);
    }
}
