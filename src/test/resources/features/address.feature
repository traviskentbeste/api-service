@address
Feature:  Cucumber: Address tests

  Background:
    Given The address is available at "http://localhost" with endpoint "/api/v1/address"

  Scenario: Check that there are at least 1 address
    When I fetch the address
    Then I should find at least 1 address