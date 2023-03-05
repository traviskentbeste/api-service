@EventSummary
Feature:  Cucumber: event-summary tests

  Background:
    Given The eventSummary is available at "http://localhost" with endpoint "/api/v1/event-summary"

  Scenario: Check that there are at least 1 event-summary
    When I fetch the event-summary
    Then I should find at least 1 event-summary