@Creditor
Feature:  Cucumber: creditor tests

  Background:
    Given The creditor is available at "http://localhost" with endpoint "/api/v1/creditor"

  Scenario: Check that there are at least 1 creditor
    When I fetch the creditor
    Then I should find at least 1 creditor