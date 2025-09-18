package stepDefinitions;

import io.cucumber.java.en.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

public class SimCardActivatorStepDefinitions {

    private final RestTemplate restTemplate = new RestTemplate();
    private ResponseEntity<String> postResponse;
    private ResponseEntity<Map> getResponse;

    @Given("the SIM card actuator is running")
    public void actuator_is_running() {
        // Make sure SimCardActuator.jar is running manually.
    }

    @When("I submit an activation request with ICCID {string} and customerEmail {string}")
    public void submit_activation_request(String iccid, String email) {
        Map<String, String> payload = Map.of(
                "iccid", iccid,
                "customerEmail", email
        );

        postResponse = restTemplate.postForEntity("http://localhost:8080/sim/activate", payload, String.class);
    }

    @Then("the activation should be successful for record ID {int}")
    public void activation_should_be_successful(int recordId) {
        getResponse = restTemplate.getForEntity("http://localhost:8080/sim/record?simCardId=" + recordId, Map.class);
        assertNotNull(getResponse.getBody());
        assertTrue((Boolean) getResponse.getBody().get("active"));
    }

    @Then("the activation should fail for record ID {int}")
    public void activation_should_fail(int recordId) {
        getResponse = restTemplate.getForEntity("http://localhost:8080/sim/record?simCardId=" + recordId, Map.class);
        assertNotNull(getResponse.getBody());
        assertFalse((Boolean) getResponse.getBody().get("active"));
    }
}
