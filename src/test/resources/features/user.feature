@user
Feature:  Cucumber: User tests

  Background:
    Given The user is available at "http://localhost" with endpoint "/api/v1/user"

  Scenario: Check that there are at least 1 user
    When I fetch the user
    Then I should find at least 1 user