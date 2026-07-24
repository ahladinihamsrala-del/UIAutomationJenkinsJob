package api.automation.usermanagement.steps;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

import api.automation.usermanagement.configmanager.ConfigManager;
import api.automation.usermanagement.context.TestContext;



public class UserStepsPost {

 	
	private static final String BASE_URI =ConfigManager.required("base.url");

 private final TestContext context;

 public UserStepsPost(TestContext context) {
     this.context = context;
 }

 

 @When("I create a dummy user and save the generated ID")
 public void createDummyUserAndSaveGeneratedId() {
     String apiKey = System.getenv("REQRES_API_KEY");

     //Create a JSON requestbody
     String name ="Morpheus";
     String job = "Leader";

     String requestBody = String.format(
             "{\"name\":\"%s\",\"job\":\"%s\"}",
             name,
             job
     );
     ExtentCucumberAdapter.addTestStepLog("Invoking POST Method for user creation");
     Response postResponse = RestAssured
             .given()
             .baseUri(BASE_URI)
             .header("x-api-key", apiKey)
             .contentType(ContentType.JSON)
             .body(requestBody)
             .when()
             .post("/api/users")
             .then()
             .extract()
             .response();

     context.setPostResponse(postResponse);//setting the context variable

     String generatedUserId = postResponse.jsonPath().getString("id");//to get the id value from JSON

     MatcherAssert.assertThat(
             "POST did not return a generated user ID. "
                     + "HTTP " + postResponse.statusCode()
                     + ". Response body: " + postResponse.asString(),
             generatedUserId,
             Matchers.not(Matchers.emptyOrNullString())
     );

     context.setCreatedUserId(generatedUserId);
 }

 @When("I retrieve the user using the generated ID")
 public void retrieveUserUsingGeneratedId() {
    
     String apiKey = System.getenv("REQRES_API_KEY");
     String generatedUserId = context.getCreatedUserId();

     MatcherAssert.assertThat(
             "Generated user ID is missing from TestContext.",
             generatedUserId,
             Matchers.not(Matchers.emptyOrNullString())
     );
     ExtentCucumberAdapter.addTestStepLog("Retrieve the  user created using GET Method");

     Response getResponse = RestAssured
             .given()
             .baseUri(BASE_URI)
             .header("x-api-key", apiKey)
             .pathParam("id", generatedUserId)
             .when()
             .get("/api/users/{id}")
             .then()
             .extract()
             .response();

     context.setGetResponse(getResponse);
 }

 @Then("the POST response status should be {int}")
 public void validatePostResponseStatus(int expectedStatus) {
     Response postResponse = context.getPostResponse();

     MatcherAssert.assertThat(
             "POST response was not stored in TestContext.",
             postResponse,
             Matchers.notNullValue()
     );

     MatcherAssert.assertThat(
             "POST status mismatch. Response body: "
                     + postResponse.asString(),
             postResponse.statusCode(),
             Matchers.equalTo(expectedStatus)
     );
     
     ExtentCucumberAdapter.addTestStepLog("POST response status Matches with " + expectedStatus);
 }

 @Then("the GET response status should match the Status column")
 public void validateGetResponseStatus() {
     Response getResponse = context.getGetResponse();

     //assert the response is not Null
     MatcherAssert.assertThat(
             "GET response was not stored in TestContext.",
             getResponse,
             Matchers.notNullValue()
     );

     String expectedStatus = context.data("status");

     MatcherAssert.assertThat(
             "Expected GET status is missing from Excel.",
             expectedStatus,
             Matchers.not(Matchers.emptyOrNullString())
     );

     String trimmedStatus = expectedStatus.trim();

  

     int expectedStatusCode = Integer.parseInt(trimmedStatus);

     MatcherAssert.assertThat(
             "GET status mismatch",                             
              getResponse.statusCode(),
             Matchers.equalTo(expectedStatusCode)
     );
     ExtentCucumberAdapter.addTestStepLog("GET response status Matches with " + expectedStatus);
 }





 
}
