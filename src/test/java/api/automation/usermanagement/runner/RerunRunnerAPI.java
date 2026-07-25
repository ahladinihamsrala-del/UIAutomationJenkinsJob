package api.automation.usermanagement.runner;

import org.testng.annotations.Listeners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * Re-executes only the API scenarios that FAILED in the previous run.
 *
 * How it works: TestRunnerAPI's plugin list must include "rerun:target/rerun-api.txt"
 * (add this to your existing @CucumberOptions plugin array - see README).
 * Cucumber writes the file:line of every failed scenario into that file after
 * each run. The "@" prefix below tells Cucumber to treat the file's contents
 * as the list of features/scenarios to run - this is native Cucumber syntax,
 * not custom logic.
 *
 * Triggered from Jenkins only when target/rerun-api.txt is non-empty (see Jenkinsfile).
 */
@CucumberOptions(
        features = "@target/rerun-api.txt",
        glue = "api.automation.usermanagement",
        plugin = {
                "pretty",
                "html:target/cucumber-report-rerun-api.html",
                "json:target/cucumber-report-rerun-api.json",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        monochrome = true
)
public class RerunRunnerAPI extends AbstractTestNGCucumberTests {
}
