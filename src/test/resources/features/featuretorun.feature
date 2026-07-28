Feature: For demo Purpose  
    
    
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
      
       Scenario Outline: Validate the flights booking workflow End to End 
   Given I am on the Home page
  When I click on Login or Signup link
   And I go to the Login page and enter the correct Mobile number and OTP
   Then I am signed in successfully and taken to the search page
   When I choose Roundtrip and flight details to search for "<Travel type>"
   Then I go to the flights results page 
   When I choose the cheapest departure and cheapest return and proceed with booking
   Then I am navigated to the Review and Traveller details page
   When I verify the flight details and airport details
   And Add Traveller information contact details and Continue
   Then I am Navigated to the Payments page
   
    Examples:
      | Travel type |
      | Economy     |