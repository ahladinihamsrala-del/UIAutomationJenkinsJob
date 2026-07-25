package listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IMPORTANT for Cucumber + AbstractTestNGCucumberTests projects:
 * All scenarios run through ONE shared @Test(dataProvider = "scenarios") method
 * (commonly named runScenario). TestNG reuses a SINGLE instance of the class set
 * as retryAnalyzer for every invocation of that method - so a plain instance
 * field for retryCount would leak across different scenarios and break retry
 * logic silently. This version keys the counter per test result identity instead.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final int MAX_RETRY = 2;
    private static final Map<String, Integer> RETRY_COUNTS = new ConcurrentHashMap<>();

    @Override
    public boolean retry(ITestResult result) {
        String key = testKey(result);
        int attempts = RETRY_COUNTS.getOrDefault(key, 0);

        if (attempts < MAX_RETRY) {
            RETRY_COUNTS.put(key, attempts + 1);
            System.out.println("Retrying scenario [" + key + "] - attempt " + (attempts + 1) + " of " + MAX_RETRY);
            return true;
        }
        return false;
    }

    /**
     * Builds a stable per-scenario key. For Cucumber-TestNG, result.getTestName()
     * or the scenario name embedded in parameters is typically available -
     * adjust this if your CucumberFeatureWrapper exposes the name differently.
     */
    private String testKey(ITestResult result) {
        String testName = result.getTestName() != null ? result.getTestName() : result.getName();
        Object[] params = result.getParameters();
        String paramSuffix = (params != null && params.length > 0) ? params[0].toString() : String.valueOf(result.hashCode());
        return testName + "::" + paramSuffix;
    }
}
