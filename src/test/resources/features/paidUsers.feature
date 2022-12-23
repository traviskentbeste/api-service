@paidUsers
Feature:  Cucumber: PaidUsers tests

  Background:
    Given The paidUser is available at "http://localhost" with endpoint "/api/v1/paid-users"

  Scenario: Check that there are at least 1 paidUsers
    When I fetch the paidUsers
    Then I should find at least 1 paidUsers