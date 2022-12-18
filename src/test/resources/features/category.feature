@category
Feature:  Cucumber: Category tests

  Background:
    Given The application is available at "http://localhost" with endpoint "/api/v1/category"
    And Health check is ok

  Scenario: Check that there are at least 1 category
    When I fetch the category
    Then I should find at least 1 category