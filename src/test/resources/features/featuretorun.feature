Feature: For demo Purpose  
    
    
    Scenario Outline: Validate if the traveller is able to search Busese in a city
    Given I am on the Home page
   When I click on Login or Signup link
   And I go to the Login page and enter the correct Mobile number and OTP
   Then I am signed in successfully and taken to the search page
   When I search for Buses in departure "<City>"
   Then I am navigated to the available Buses page
   
   
   Examples:
      | City |
      | Chennai| 