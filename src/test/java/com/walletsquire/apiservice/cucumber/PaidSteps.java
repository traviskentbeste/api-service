package com.walletsquire.apiservice.cucumber;

import com.google.gson.Gson;
import com.walletsquire.apiservice.entities.Paid;
import com.walletsquire.apiservice.entities.Health;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PaidSteps {

    @Autowired
    private CucumberHttpClient cucumberHttpClient;

    private String requestUrl;
    private String requestEndpoint;
    private List<Paid> entityList = new ArrayList<>();

    @Given("The paid is available at {string} with endpoint {string}")
    public void theApplicationIsAvailableAt(String url, String endpoint) throws IOException {

        requestUrl = url;
        requestEndpoint = endpoint;
        ResponseEntity<String> response = cucumberHttpClient.getAll(requestUrl, requestEndpoint);
        assertEquals(response.getStatusCodeValue(), 200);

    }

    @When("I fetch the paid")
    public void iFetchArray() {

        ResponseEntity<String> response = cucumberHttpClient.getAll(requestUrl, requestEndpoint);
        Gson gson = new Gson();
        entityList = gson.fromJson(response.getBody(), ArrayList.class);

    }

    @Then("I should find at least {int} paid")
    public void iShouldFindArray(int count) {

        assertTrue(entityList.size() >= count );

    }

}
