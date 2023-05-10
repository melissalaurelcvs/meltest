Feature: App Health Check

  Scenario: App is UP
    Given a running app
    When a GET request is made to its health check endpoint
    Then the response is returned with 200 status code
    And the status is "UP"
