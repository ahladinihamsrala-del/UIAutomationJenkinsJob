Feature: Bus booking  
 
  @loggedIn
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
   @loggedIn
   Scenario Outline: Validate if the Bus search results are returned as per the applied Bus type 
    Given I am on the Home page
   When I click on Login or Signup link
   And I go to the Login page and enter the correct Mobile number and OTP
   Then I am signed in successfully and taken to the search page
   When I search for Buses in departure "<City>"
   Then I am navigated to the available Buses page
   When I apply Bus type filter and choose Bus Parnter
   Then Search results show the applied bus type and Chosen Bus partner
   
   
   
   Examples:
      | City |
      | Chennai|
    @loggedIn     
    Scenario Outline: E2E Validate if the user is able to navigate till the bus Payment page  
    Given I am on the Home page
   When I click on Login or Signup link
   And I go to the Login page and enter the correct Mobile number and OTP
   Then I am signed in successfully and taken to the search page
   When I search for Buses in departure "<City>"
   Then I am navigated to the available Buses page
   When I apply Bus type filter and choose Bus Parnter
   Then Search results show the applied bus type and Chosen Bus partner
   When I choose the boarding and Departure points and book bus
   And Enter Passenger information
   Then I am navigated to the Bus Payments page
   
   
   Examples:
      | City |
      | Chennai|     