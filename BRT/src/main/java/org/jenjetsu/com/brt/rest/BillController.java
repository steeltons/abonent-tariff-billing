package org.jenjetsu.com.brt.rest;

import lombok.AllArgsConstructor;
import org.jenjetsu.com.brt.broker.CdrMessageSender;
import org.jenjetsu.com.core.entity.HealthCheck;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Locale;

/**
 * <h2>Bill Controller</h2>
 * @deprecated I don't know why this controller exist. Same functional has AbonentController.
 */
@RestController
@RequestMapping("/bill")
@AllArgsConstructor
@Deprecated(forRemoval = true)
public class BillController {

    private final String CDR_HEALTH_CHECK_PATH = "http://localhost:8100/actuator/health";
    private final CdrMessageSender cdrMessageSender;

    @PostMapping("start-billing")
    public ResponseEntity<?> startBilling(@RequestBody String billCommand) {
        RestTemplate restTemplate = new RestTemplate();
        HealthCheck check = restTemplate.getForObject(CDR_HEALTH_CHECK_PATH, HealthCheck.class);
        cdrMessageSender.sendCdrCommand(billCommand);
        String message = "";
        if(!check.status().toUpperCase(Locale.ROOT).equals("UP")) {
            message = "CDR is not working now, but message to collect was sent";
        } else {
            message = "Start billing process";
        }
        return ResponseEntity.ok(message);
    }
}
