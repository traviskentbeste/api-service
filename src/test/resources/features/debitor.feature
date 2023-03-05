@Debitor
Feature:  Cucumber: debitor tests

  Background:
    Given The debitor is available at "http://localhost" with endpoint "/api/v1/debitor"

  Scenario: Check that there are at least 1 debitor
    When I fetch the debitor
    Then I should find at least 1 debitor