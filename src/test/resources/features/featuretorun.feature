Feature: For demo Purpose  
  
    
    Feature: Flight booking workflo
@loggedIn @chrome @firefox
Scenario Outline: Validate the flight search functionality 
    Given I am on the Home page
   When I click on Login or Signup link
   And I go to the Login page and enter the correct Mobile number and OTP
   Then I am signed in successfully and taken to the search page
   When I choose Roundtrip and flight details to search for "<Travel type>"
   Then I go to the flights results page 
   
   
   Examples:
      | Travel type |
      | Economy     |
      
  