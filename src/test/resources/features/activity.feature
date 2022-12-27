@activity
Feature:  Cucumber: Activity tests

  Background:
    Given The activity is available at "http://localhost" with endpoint "/api/v1/activity"

  Scenario: Check that there are at least 1 activity
    When I fetch the activity
    Then I should find at least 1 activity