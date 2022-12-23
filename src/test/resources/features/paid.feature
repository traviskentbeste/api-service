@paid
Feature:  Cucumber: Paid tests

  Background:
    Given The paid is available at "http://localhost" with endpoint "/api/v1/paid"

  Scenario: Check that there are at least 1 paid
    When I fetch the paid
    Then I should find at least 1 paid