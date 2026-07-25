package utils;

import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


 
public class ReportEmailTrigger {

    public static void main(String[] args) {
        String reportPath = args.length > 0 ? args[0] : "test-output/SparkReport/Spark.html";
        String resultsPath = args.length > 1 ? args[1] : "test-output/testng-results.xml";
        String subjectPrefix = args.length > 2 ? args[2] : "[Automation Report]";

        Summary summary = readSummary(resultsPath);
        String status = summary.total == 0 ? "UNKNOWN" : (summary.failed > 0 ? "FAILED" : "PASSED");
        String timestamp = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(new Date());

        String subject = subjectPrefix + " Execution " + status + " - " + timestamp;

        String body = "<h2>Automation Execution Summary</h2>"
                + "<p><b>Executed At:</b> " + timestamp + "</p>"
                + "<table border='1' cellpadding='6' style='border-collapse:collapse;'>"
                + "<tr><th>Total</th><th>Passed</th><th>Failed</th><th>Skipped</th></tr>"
                + "<tr>"
                + "<td style='text-align:center;'>" + summary.total + "</td>"
                + "<td style='color:green;text-align:center;'>" + summary.passed + "</td>"
                + "<td style='color:red;text-align:center;'>" + summary.failed + "</td>"
                + "<td style='color:orange;text-align:center;'>" + summary.skipped + "</td>"
                + "</tr></table>"
                + "<p>Full scenario-level details are in the attached Extent Spark Report.</p>";

        String attachment = new File(reportPath).exists() ? reportPath : null;
        if (attachment == null) {
            System.err.println("ReportEmailTrigger: Spark report not found at " + reportPath + " - sending email without attachment.");
        }

        EmailUtility.sendReportEmail(subject, body, attachment);
    }

    /**
     * TestNG always writes test-output/testng-results.xml after a run, with
     * counts on the root element: <testng-results total=".." passed=".." failed=".." skipped="..">
     * This is generated regardless of Selenium/API/Cucumber, so it's a reliable
     * source of pass/fail counts without adding a JSON-parsing dependency.
     */
    private static Summary readSummary(String path) {
        Summary s = new Summary();
        File file = new File(path);
        if (!file.exists()) {
            System.err.println("ReportEmailTrigger: " + path + " not found - counts will show as 0.");
            return s;
        }
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Element root = builder.parse(file).getDocumentElement();
            s.total = parseIntSafe(root.getAttribute("total"));
            s.passed = parseIntSafe(root.getAttribute("passed"));
            s.failed = parseIntSafe(root.getAttribute("failed"));
            s.skipped = parseIntSafe(root.getAttribute("skipped"));
        } catch (Exception e) {
            System.err.println("ReportEmailTrigger: failed to parse " + path + " - " + e.getMessage());
        }
        return s;
    }

    private static int parseIntSafe(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    private static class Summary {
        int total, passed, failed, skipped;
    }
}
