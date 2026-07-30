package com.ixigo.travelbooking.stepdefinitions;

import java.io.IOException;

import org.testng.Assert;

import com.ixigo.travelbooking.driver.DriverManager;
import com.ixigo.travelbooking.pages.HomePage;
import com.ixigo.travelbooking.util.PropertyFileReader;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginTestSteps {
	private HomePage homePage;
	
	
	PropertyFileReader prop=new PropertyFileReader();
	
	@Given("I am on the Home page")
	public void i_am_on_the_home_page() throws IOException {
	homePage = new HomePage(DriverManager.getDriver());
	//Assert.assertTrue(homePage.verifyHomePage().trim().equalsIgnoreCase(prop.getFromPropertyFile("url")),"User not on HomePage");
	    
	}

	@When("I click on Login or Signup link")
	public void i_click_on_login_or_signup_link() throws IOException {
	//	homePage.openSignIn();
	}
	@When("I go to the Login page and enter the correct Mobile number and OTP")
	public void i_go_to_the_login_page_and_enter_the_correct_mobile_number_and_otp() throws InterruptedException, IOException {
		//homePage.loginFromHomePageMobile();
	}

	

	@When("I go to the Login page and enter the correct Email Id and Password")
	public void i_go_to_the_login_page_and_enter_the_correct_email_id_and_password() throws InterruptedException, IOException {
		//homePage.loginFromHomePageGmail();
	}

	
	@Then("I am signed in successfully and taken to the search page")
	public void i_am_signed_in_successfully_and_taken_to_the_search_page() {
		
		String logintxt=homePage.verifyLoginText();
		//System.out.println("logintxt" +logintxt);
		
		Assert.assertTrue(logintxt.contains("Hey"));
	}

}
