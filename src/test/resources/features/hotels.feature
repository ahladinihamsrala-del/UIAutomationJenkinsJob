Feature: Hotel booking  
 
  @loggedIn @chrome @firefox
  Scenario Outline: Validate if the traveller is able to search hotels in a city
    Given I am on the Home page
   When I click on Login or Signup link
   And I go to the Login page and enter the correct Mobile number and OTP
   Then I am signed in successfully and taken to the search page
   When I search for Hotels in "<City>"
   Then I am navigated to the available hotels list page
   
   #To verify the auto push revert
   
   Examples:
      | City |
      | Hyderabad|
      
    @loggedIn @chrome @firefox  
   Scenario Outline: Validate the sort filters in  search hotel results in a city
   Given I am on the Home page
   When I click on Login or Signup link
   And I go to the Login page and enter the correct Mobile number and OTP
   Then I am signed in successfully and taken to the search page
   When I search for Hotels in "<City>"
   Then I am navigated to the available hotels list page
    When I sort by the Prices Low to High 
   Then hotels are displayed from Low to high prices
   
   Examples:
      | City |
      | Hyderabad|
      
     @loggedIn @chrome @firefox  
    Scenario Outline: Validate the Exceptional rating filters in  search hotel results in a city
   Given I am on the Home page
   When I click on Login or Signup link
   And I go to the Login page and enter the correct Mobile number and OTP
   Then I am signed in successfully and taken to the search page
   When I search for Hotels in "<City>"
   Then I am navigated to the available hotels list page
   When I apply the Exceptional Rating filter
   Then I can see all the Hotels rated Exceptional
   
   Examples:
      | City |
      | Hyderabad|
      
   @loggedIn @chrome @firefox   
   Scenario Outline: Validate the seach by locality function in search hotel results in a city
   Given I am on the Home page
   When I click on Login or Signup link
   And I go to the Login page and enter the correct Mobile number and OTP
   Then I am signed in successfully and taken to the search page
   When I search for Hotels in "<City>"
   Then I am navigated to the available hotels list page
   When I search by locality in the search box
   Then I am navigated to the hotels by locality
   
   Examples:
      | City |
      | Hyderabad|
      @loggedIn @chrome @firefox 
       Scenario Outline: Validate the Facility filters in  search hotel results in a city
   Given I am on the Home page
   When I click on Login or Signup link
   And I go to the Login page and enter the correct Mobile number and OTP
   Then I am signed in successfully and taken to the search page
   When I search for Hotels in "<City>"
   Then I am navigated to the available hotels list page
   When I apply the Facility filter for wiFi 
   Then I can see all the Hotels with the facility
   
   
   Examples:
      | City |
      | Hyderabad|
      @loggedIn @chrome @firefox 
       Scenario Outline: Validate the Best price guarantee filter for hotel results 
   Given I am on the Home page
   When I click on Login or Signup link
   And I go to the Login page and enter the correct Mobile number and OTP
   Then I am signed in successfully and taken to the search page
   When I search for Hotels in "<City>"
   Then I am navigated to the available hotels list page
   When I apply the Best price guarantee filter
   Then I can see all the Hotels with Best Price guarantee card
   
   
   Examples:
      | City |
      | Hyderabad|
     
      @loggedIn @chrome 
      Scenario Outline: Validate if the user is able to book Hotel room successfully E2E 
   Given I am on the Home page
   When I click on Login or Signup link
   And I go to the Login page and enter the correct Mobile number and OTP
   Then I am signed in successfully and taken to the search page
   When I search for Hotels in "<City>"
   Then I am navigated to the available hotels list page
   When I apply the Best price guarantee filter
   Then I can see all the Hotels with Best Price guarantee card
   When I book the best guarantee hotel reserve room
   And Add User details 
   Then I am navigated to the payment page 
   
   
   Examples:
      | City |
      |Hyderabad|
     
   