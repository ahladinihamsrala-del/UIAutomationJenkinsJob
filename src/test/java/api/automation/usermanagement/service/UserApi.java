package api.automation.usermanagement.service;

import api.automation.usermanagement.configmanager.ConfigManager;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

import com.aventstack.extentreports.ExtentTest;

public class UserApi {
 private final RequestSpecification requestSpecification;//holds the request settings

 
//configuring the API attributes 
 public UserApi() {
	 

     String apiKey = System.getenv("REQRES_API_KEY");//configured in the environment variable in the Run configurations

     if (apiKey == null || apiKey.isBlank()) {
         throw new IllegalStateException(
                 "Set REQRES_API_KEY to proceed" );
     }

     requestSpecification = new RequestSpecBuilder()//builds the request 
             .setBaseUri(
                     ConfigManager.required("base.url"))//getting from Config properties file 
             .setContentType(ContentType.JSON)
             .setAccept(ContentType.JSON)
             .addHeader("x-api-key", apiKey)
             .build();
 }

 public Response getUser(String userId) {
     return given()
             .spec(requestSpecification) //pass the request built to mimic GET request 
             .pathParam("id", userId)
             .when()
             .get("/api/users/{id}");
 }

 
}

