package au.com.telstra.simcardactivator;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/sim")
public class SimActivationController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String actuatorUrl = "http://localhost:8444/actuate";

    @Autowired
    private SimCardRecordRepository repository;

    // POST endpoint to activate SIM
    @PostMapping("/activate")
    public ResponseEntity<String> activateSim(@RequestBody SimActivationRequest request) {

        // Prepare payload for actuator
        Map<String, String> payload = Map.of("iccid", request.getIccid());

        // Send request to actuator
        ActuatorResponse response = restTemplate.postForObject(actuatorUrl, payload, ActuatorResponse.class);

        boolean success = response != null && response.isSuccess();

        // Save record to database
        SimCardRecord record = new SimCardRecord(request.getIccid(), request.getCustomerEmail(), success);
        repository.save(record);

        return success
                ? ResponseEntity.ok("Activation successful")
                : ResponseEntity.status(500).body("Activation failed");
    }

    // GET endpoint to fetch SIM card record by ID
    @GetMapping("/record")
    public ResponseEntity<SimCardRecord> getSimCardRecord(@RequestParam Long simCardId) {
        return repository.findById(simCardId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
