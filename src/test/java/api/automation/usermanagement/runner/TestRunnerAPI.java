package api.automation.usermanagement.runner;

import org.testng.annotations.Listeners;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
     features = "src/test/resources/features/user_get.feature",
     glue = "api.automation.usermanagement",
     plugin = {
             "pretty",
             "html:target/cucumber-report.html",
             "json:target/cucumber-report.json",
             "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
     },
     monochrome = true
)

public class TestRunnerAPI
     extends AbstractTestNGCucumberTests {
}
