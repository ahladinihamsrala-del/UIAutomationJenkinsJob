Feature: User Login workflow


  @loggedIn
  Scenario Outline: Validate the login functionality of the travel site
    Given I am on the Home page
   When I click on Login or Signup link
   And I go to the Login page and enter the correct Mobile number and OTP
    Then I am signed in successfully and taken to the search page

    #verify merger 

  
  
  