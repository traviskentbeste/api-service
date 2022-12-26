@event
Feature:  Cucumber: Event tests

  Background:
    Given The application is available at "http://localhost" with endpoint "/api/v1/event"

  Scenario: Check that there are at least 1 event
    When I fetch the event
    Then I should find at least 1 event