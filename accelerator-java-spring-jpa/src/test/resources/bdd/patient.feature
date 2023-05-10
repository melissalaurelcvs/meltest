Feature: Patient
  Scenario: Create a patient
    Given a user provides patient firstName "some-patient-first-name" and lastName "some-patient-last-name" and email "patiant@example.com"
    When a post request is made
    Then a response is returned with 201 status code

  Scenario: Get a patient
    Given a patient with firstName "some-patient-first-name" and lastName "some-patient-last-name" and email "patiant@example.com" exists
    When a get request is made for that patient's ID
    Then a response is returned with 200 status code
    And the body contains the patient with firstName "some-patient-first-name" and lastName "some-patient-last-name" and email "patiant@example.com"

  Scenario: Update a patient
    Given a patient with firstName "some-patient-first-name" and lastName "some-patient-last-name" and email "patiant@example.com" exists
    Given a user update patient with firstName "some-patient-first-name" and lastName "some-patient-other-last-name" and email "patiant@example.com"
    When a put request is made for that patient's ID
    Then a response is returned with 200 status code
    And the body contains the patient with firstName "some-patient-first-name" and lastName "some-patient-other-last-name" and email "patiant@example.com"

  Scenario: Get a list of patients
    Given a patient with firstName "some-patient-first-name" and lastName "some-patient-last-name" and email "patiant@example.com" exists
    When a get request is made to retrieve a list of patients
    Then a response is returned with 200 status code
    And the body contains a list with the patient with firstName "some-patient-first-name" and lastName "some-patient-last-name" and email "patiant@example.com"

  Scenario: Delete a patient
    Given a patient with firstName "some-patient-first-name" and lastName "some-patient-last-name" and email "patiant@example.com" exists
    When a delete request is made
    Then a response is returned with 200 status code
