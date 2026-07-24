package api.automation.usermanagement.steps;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import api.automation.usermanagement.configmanager.ConfigManager;
import api.automation.usermanagement.context.TestContext;
import api.automation.usermanagement.service.UserApi;
import api.automation.usermanagement.utils.ExcelFileUtil;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class UserSteps {
	private final TestContext context;
	private final UserApi userApi;

	public UserSteps(TestContext context) {
		this.context = context;
		this.userApi = new UserApi();
	}

	@Given("I load user data for testcase {string}")
	public void loadUserData(String testCase) {
		try {
			System.out.println("from cucumber "+testCase);
			
			//Takes Excel file path , sheetname and test case ID to read the excel contents
			Map<String, String> data = ExcelFileUtil.readByTestCase(Path.of(ConfigManager.required("user.info.file")),
					ConfigManager.required("user.info.sheet"), testCase);
			ExtentCucumberAdapter.addTestStepLog("Test data retrieved from UserInfo Excel " );
			context.setUserData(data);//saving it to the context object
		} catch (IOException exception) {
			throw new IllegalStateException("Unable to read UserInfo.xlsx.", exception);
		}
	}

	@When("I send a GET request using the User ID from UserInfo Excel")
	public void sendGetRequest() {
		String userId = context.data("userid");//save the user ID from the context object

		if (userId.isBlank()) {
			throw new IllegalStateException("User ID is blank in UserInfo.xlsx.");
		}
		//invoking the service to perform Get operation and storing
				//the get response in the context object
		
		ExtentCucumberAdapter.addTestStepLog("Invoking the GET operation for user : " + userId);
		context.setGetResponse(userApi.getUser(userId));
	}

	@Then("the response status should match the Status column")
	public void validateResponseStatus() {
		Response response = context.getGetResponse();//retrieving the response from the context object

		int expectedStatus = Integer.parseInt(context.data("status"));// retrieving the response code

		MatcherAssert.assertThat("GET response status does not match " + "UserInfo.xlsx", response.statusCode(),
				Matchers.equalTo(expectedStatus));
		ExtentCucumberAdapter.addTestStepLog("GET response status Matches with " + expectedStatus);
		
	}

	@Then("the response should contain the selected user fields")
	public void validateUserFields() {
		Response response = context.getGetResponse();

		if (response.statusCode() == 404) {
			MatcherAssert.assertThat("Expected empty response for 404", response.asString().trim(),
					Matchers.equalTo("{}"));
			return;
		}

		if (response.statusCode() != 200) {
			return;
		}

		int expectedUserId = Integer.parseInt(context.data("userid"));

		response.then().body("data.id", Matchers.equalTo(expectedUserId))
				.body("data.email",
						Matchers.allOf(Matchers.not(Matchers.blankOrNullString()), Matchers.containsString("@")))
				.body("data.first_name", Matchers.not(Matchers.blankOrNullString()))
				.body("data.last_name", Matchers.not(Matchers.blankOrNullString())).body("data.avatar",
						Matchers.allOf(Matchers.not(Matchers.blankOrNullString()), Matchers.containsString("http")));
	
		ExtentCucumberAdapter.addTestStepLog("User information retrivers from response is as expected for user " + expectedUserId);
	}
	
	
	
	@When("I send a GET request without an API key")
	public void sendGetRequestWithoutApiKey() {
	    String userId = context.data("userid");

	    ExtentCucumberAdapter.addTestStepLog("Inoking the GET operation without API key");
	    Response response = RestAssured
	            .given()
	            .baseUri("https://reqres.in")
	            .header("User-Agent", "api-automation-tests/1.0")
	            .accept("application/json")
	            .when()
	            .get("/api/users/{userId}", userId)
	            .then()
	            .extract()
	            .response();
	    
	    context.setGetResponse(response);
	}
	
	@Then("the response status should match the Status column as unauthorised")
	public void the_response_status_should_match_the_status_column_as_unauthorised() {
		  Response response = context.getGetResponse();

		    MatcherAssert.assertThat(
		            "No response was stored in TestContext.",
		            response,
		            Matchers.notNullValue()
		    );

		    String expectedStatus = context.data("status");
		    String trimmedExpectedStatus =
		            expectedStatus == null ? null : expectedStatus.trim();

		    MatcherAssert.assertThat(
		            "Expected status is missing from Excel.",
		            trimmedExpectedStatus,
		            Matchers.notNullValue()
		    );

		   

		    int expectedStatusCode = Integer.parseInt(trimmedExpectedStatus);
		    int actualStatusCode = response.statusCode();

		    MatcherAssert.assertThat(
		            "HTTP status mismatch. Response body: " + response.asString(),
		            actualStatusCode,
		            Matchers.equalTo(expectedStatusCode)
		    );
		  
		    ExtentCucumberAdapter.addTestStepLog("GET response status Matches with " + expectedStatus);    
	}
	
	@When("I send a malformed GET request")
	public void sendMalformedPostRequest() {
	    String apiKey = System.getProperty("reqres.api.key",
	            System.getenv("REQRES_API_KEY"));

	    if (apiKey == null || apiKey.isBlank()) {
	        throw new IllegalStateException(
	                "Missing ReqRes API key. Configure reqres.api.key or REQRES_API_KEY.");
	    }
	    ExtentCucumberAdapter.addTestStepLog("Inoking the GET operation without incorrect request body");
	    Response response = RestAssured
	            .given()
	            .baseUri("https://reqres.in")
	            .header("x-api-key", apiKey)
	            .header("X-Reqres-Env", "prod")
	            .header("User-Agent", "api-automation-tests/1.0")
	            .header("Accept", "application/json")
	            .header("Content-Type", "application/json")
	            .body("{\"name\":\"John\",\"job\":")//broken JSON in order to get 400 bad request
	            .when()
	            .post("/api/users")
	            .then()
	            .extract()
	            .response();

	    context.setGetResponse(response);
	}
	
	@Then("the response status should match the Status column as Bad request")
	public void the_response_status_should_match_the_status_column_as_Bad_request() {

	    Response response = context.getGetResponse();

	    MatcherAssert.assertThat(
	            "No response was stored in TestContext.",
	            response,
	            Matchers.notNullValue()
	    );

	    String expectedStatus = context.data("status");
	    String trimmedExpectedStatus =
	            expectedStatus == null ? null : expectedStatus.trim();

	    MatcherAssert.assertThat(
	            "Expected status is missing from Excel.",
	            trimmedExpectedStatus,
	            Matchers.notNullValue()
	    );

	   

	    int expectedStatusCode = Integer.parseInt(trimmedExpectedStatus);
	    int actualStatusCode = response.statusCode();

	    MatcherAssert.assertThat(
	            "HTTP status mismatch. Response body: " + response.asString(),
	            actualStatusCode,
	            Matchers.equalTo(expectedStatusCode)
	    );
	    ExtentCucumberAdapter.addTestStepLog("GET response status Matches with " + expectedStatus);   
	}


}
