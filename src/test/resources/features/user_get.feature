Feature: Create new users and Retrieve existing ReqRes users

  @api
  Scenario: Create a user and retrieve it using the generated ID
    Given I load user data for testcase "TC_User_Create_Post"
    When I create a dummy user and save the generated ID
    And I retrieve the user using the generated ID
    Then the POST response status should be 201
    And the GET response status should match the Status column

  @api
  Scenario Outline: Retrieve an existing user using UserInfo Excel data and return 200 response
    Given I load user data for testcase "<testcase>"
    When I send a GET request using the User ID from UserInfo Excel
    Then the response status should match the Status column
    And the response should contain the selected user fields

    Examples:
      | testcase   |
      | TC_User_03 |
      | TC_User_10 |
      | TC_User_09 |
      | TC_User_12 |

  @api
  Scenario Outline: Retrieve an non existing user using UserInfo Excel data and return 404 response code
    Given I load user data for testcase "<testcase>"
    When I send a GET request using the User ID from UserInfo Excel
    Then the response status should match the Status column
    And the response should contain the selected user fields

    Examples:
      | testcase   |
      | TC_User_20 |
      | TC_User_15 |
      | TC_User_19 |

  @api
  Scenario: Reject GET request when API key is missing with a 401 response code
    Given I load user data for testcase "TC_User_401"
    When I send a GET request without an API key
    Then the response status should match the Status column as unauthorised

  @api
  Scenario: Reject GET request when malformed body is sent with a 400 response code
    Given I load user data for testcase "TC_User_400"
    When I send a malformed GET request
    Then the response status should match the Status column as Bad request
