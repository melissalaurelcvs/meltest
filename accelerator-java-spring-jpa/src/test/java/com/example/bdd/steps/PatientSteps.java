package com.example.bdd.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONParser;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PatientSteps {

    private static final String BASE_URL = "/api/patient/";
    private String createPatientRequestJson;
    private String updatePatientRequestJson;
    private Response response;
    private UUID patientId;

    @Given("a user provides patient firstName {string} and lastName {string} and email {string}")
    public void providePatientDetails(String firstName, String lastName, String email) {
        createPatientRequestJson = String.format("{\"firstName\": \"%s\",\"lastName\": \"%s\", \"email\": \"%s\"}", firstName, lastName, email);
    }

    @Given("a user update patient with firstName {string} and lastName {string} and email {string}")
    public void provideUpdatePatientDetails(String firstName, String lastName, String email) {
        updatePatientRequestJson = String.format("{\"firstName\": \"%s\",\"lastName\": \"%s\", \"email\": \"%s\"}", firstName, lastName, email);
    }

    @Given("a patient with firstName {string} and lastName {string} and email {string} exists")
    public void createPatient(String firstName, String lastName, String email) {
        providePatientDetails(firstName, lastName, email);
        postRequestToCreatePatient();
    }

    @When("a post request is made")
    public void postRequestToCreatePatient() {
        response =
                given()
                    .contentType("application/json")
                    .body(createPatientRequestJson)
                .when()
                    .post("/api/patient")
                .then()
                    .extract().response();

        String locationHeader = response.header("Location");
        Pattern locationPattern = Pattern.compile("^http://localhost:8080/api/patient/([a-f0-9\\-]+)$");
        Matcher matcher = locationPattern.matcher(locationHeader);
        assertThat(matcher.matches()).isTrue();
        String group = matcher.group(1);
        patientId = UUID.fromString(group);
    }

    @When("a put request is made for that patient's ID")
    public void updatePatientRequest() {
        response =
                given()
                        .contentType("application/json")
                        .body(updatePatientRequestJson)
                        .when()
                        .put("/api/patient/" + patientId)
                        .then()
                        .extract().response();
    }

    @When("a get request is made for that patient's ID")
    public void getPatientRequest() {
        response =
                given()
                .when()
                    .get("/api/patient/" + patientId)
                .then()
                    .extract().response();
    }

    @When("a get request is made to retrieve a list of patients")
    public void listPatientsRequest() {
        response =
                given()
                .when()
                    .get("/api/patient")
                .then()
                    .extract().response();
    }

    @When("a delete request is made")
    public void deletePatientRequest() {
        response =
                given()
                .when()
                    .delete(BASE_URL + patientId)
                .then()
                    .extract().response();
    }

    @Then("a response is returned with {int} status code")
    public void assertStatusCode(int expectedStatusCode) {
        assertThat(response.statusCode()).isEqualTo(expectedStatusCode);
    }

    @Then("the body contains the patient with firstName {string} and lastName {string} and email {string}")
    public void assertContainsPatient(String expectedPatientFirstName, String expectedPatientLastName, String expectedPatientEmail) throws JSONException {
        String body = response.getBody().print();
        String expectedResponseJson = patientResponseJson(expectedPatientFirstName, expectedPatientLastName, expectedPatientEmail);
        JSONAssert.assertEquals(expectedResponseJson, body, false);
    }

    @Then("the body contains a list with the patient with firstName {string} and lastName {string} and email {string}")
    public void assertContainsListWithPatient(String expectedPatientFirstName, String expectedPatientLastName, String expectedPatientEmail) throws JSONException {
        String body = response.getBody().print();
        String patientResponseJson = patientResponseJson(expectedPatientFirstName, expectedPatientLastName, expectedPatientEmail);
        JSONArray array = (JSONArray) JSONParser.parseJSON(body);
        jsonListContains(array, patientResponseJson);
    }

    private void jsonListContains(JSONArray array, String patientResponseJson) throws JSONException {
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONAssert.assertEquals(patientResponseJson, array.getJSONObject(i), false);
                return;
            } catch (AssertionError ignore) {
            }
        }
        throw new AssertionError("Json array does not contain expected Json object");
    }

    private String patientResponseJson(String patientFirstName, String patientLastName, String patientEmail) {
        return String.format("{\"firstName\": \"%s\", \"lastName\": \"%s\",\"email\" : \"%s\"}", patientFirstName, patientLastName, patientEmail);
    }
}
