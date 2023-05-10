package com.example.bdd.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.skyscreamer.jsonassert.JSONAssert;

import static io.restassured.RestAssured.when;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AppHealthCheckSteps {

    private Response response;

    @Given("a running app")
    public void runningApp() {
        //NOOP
    }

    @When("a GET request is made to its health check endpoint")
    public void getHealthCheck() {
        response =
            when()
                .get("/actuator/health")
            .then()
                .extract().response();
    }

    @Then("the response is returned with {int} status code")
    public void assertStatusCode(int expectedStatusCode) {
        assertThat(response.statusCode()).isEqualTo(expectedStatusCode);
    }

    @Then("the status is {string}")
    public void assertResponseBody(String expectedStatus) throws Exception {
        String body = response.getBody().print();
        String expectedResponseJson = String.format("{\"status\":\"" + expectedStatus + "\"}");
        JSONAssert.assertEquals(expectedResponseJson, body, false);
    }
}
