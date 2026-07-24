package api.automation.usermanagement.hooks;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import api.automation.usermanagement.configmanager.ConfigManager;
import api.automation.usermanagement.context.TestContext;
import api.automation.usermanagement.utils.ExcelFileUtil;

import io.cucumber.java.After;
import io.restassured.response.Response;

public class ResultHooks {

	private final TestContext context;

	public ResultHooks(TestContext context) {
		this.context = context;
	}

	@After
	public void writeGetResult() {
		Path resultsPath = Path.of(ConfigManager.required("results.file")).toAbsolutePath().normalize();

		String sheetName = ConfigManager.required("results.sheet");

		Response response = context.getGetResponse();

		System.out.println("[ResultHooks] Hook executed");
		System.out.println("[ResultHooks] Results path: " + resultsPath);
		System.out.println("[ResultHooks] Sheet: " + sheetName);
		System.out.println("[ResultHooks] Response: " + (response == null ? "NULL" : response.statusCode()));

		if (response == null) {
			System.err.println("[ResultHooks] No response found. " + "No result was written.");
			return;
		}

		Map<String, String> result = new LinkedHashMap<>();

		result.put("TestCase", context.data("testcase"));
		result.put("UserID", context.data("userid"));
		result.put("ResponseStatus", String.valueOf(response.statusCode()));
		result.put("Id", responseValue(response, "data.id"));
		result.put("Email", responseValue(response, "data.email"));
		result.put("FirstName", responseValue(response, "data.first_name"));
		result.put("LastName", responseValue(response, "data.last_name"));
		result.put("Avatar", responseValue(response, "data.avatar"));

		try {
			ExcelFileUtil.appendResult(resultsPath, sheetName, result);

			System.out.println("[ResultHooks] Result appended successfully");

			if (Files.exists(resultsPath)) {
				System.out.println("[ResultHooks] File size after write: " + Files.size(resultsPath) + " bytes");
			}
		} catch (IOException exception) {
			throw new UncheckedIOException("Unable to write Results.xlsx at " + resultsPath, exception);
		}
	}

	private static String responseValue(Response response, String jsonPath) {

		try {
			String value = response.jsonPath().getString(jsonPath);

			return value == null ? "" : value;
		} catch (RuntimeException exception) {
			return "";
		}
	}
}
