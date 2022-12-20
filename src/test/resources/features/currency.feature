@currency
Feature:  Cucumber: Currency tests

  Background:
    Given The currency is available at "http://localhost" with endpoint "/api/v1/currency"

  Scenario: Check that there are at least 1 currency
    When I fetch the currency
    Then I should find at least 1 currency