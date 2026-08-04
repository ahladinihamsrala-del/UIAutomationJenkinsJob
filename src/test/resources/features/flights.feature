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
      
      @loggedIn @chrome @firefox 
      Scenario Outline: Validate the journey search details are displayed in the header 
    Given I am on the Home page
   When I click on Login or Signup link
   And I go to the Login page and enter the correct Mobile number and OTP
   Then I am signed in successfully and taken to the search page
   When I choose Roundtrip and flight details to search for "<Travel type>"
   Then I go to the flights results page 
   Then validate the flight search information is displayed in the header for "<Travel type>" passenger
   
   Examples:
      | Travel type |
      | Economy     |
      
    @loggedIn @chrome  @firefox 
   Scenario Outline: Validate the flights are displayed in the results page as per the Recommendation filter applied 
   Given I am on the Home page
   When I click on Login or Signup link
   And I go to the Login page and enter the correct Mobile number and OTP
   Then I am signed in successfully and taken to the search page
   When I choose Roundtrip and flight details to search for "<Travel type>"
   Then I go to the flights results page 
   When I apply the recommended filter
   Then all the results are shown as per the filter applied 
   
    Examples:
      | Travel type |
      | Economy     |
      
      @loggedIn @chrome @firefox 
      Scenario Outline: Validate the flights are displayed in the results page as per the Sort filter applied 
    Given I am on the Home page
   When I click on Login or Signup link
   And I go to the Login page and enter the correct Mobile number and OTP
   Then I am signed in successfully and taken to the search page
   When I choose Roundtrip and flight details to search for "<Travel type>"
   Then I go to the flights results page 
   When I apply the sort filter
   Then all the results are shown as per the filter applied 
   
    Examples:
      | Travel type |
      | Economy     |
      
      @loggedIn @chrome @firefox 
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
 
 