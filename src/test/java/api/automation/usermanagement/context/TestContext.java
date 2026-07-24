package api.automation.usermanagement.context;


import java.util.LinkedHashMap;
import java.util.Map;

import io.restassured.response.Response;

public class TestContext {

 private final Map<String, String> userData =
         new LinkedHashMap<>();

 private Response getResponse;
 private Response postResponse;
 private String createdUserId;

 public void setUserData(Map<String, String> data) {
     userData.clear();
     userData.putAll(data);
 }

 public String data(String key) {
     return userData.getOrDefault(key, "");
 }

 public void setGetResponse(Response response) {
     getResponse = response;
 }

 public Response getGetResponse() {
     return getResponse;
 }

 public void setPostResponse(Response response) {
     postResponse = response;
 }

 public Response getPostResponse() {
     return postResponse;
 }

 public void setCreatedUserId(String createdUserId) {
     this.createdUserId = createdUserId;
 }

 public String getCreatedUserId() {
     return createdUserId;
 }
}
