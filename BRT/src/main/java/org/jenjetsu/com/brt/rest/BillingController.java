package org.jenjetsu.com.brt.rest;

import org.jenjetsu.com.brt.billing.BillingProcess;
import org.jenjetsu.com.brt.billing.BillingProcessInformation;
import org.jenjetsu.com.brt.billing.BillingStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/billing")
public class BillingController {

    private final BillingProcess<?> billingProcess;

    public BillingController(@Qualifier("standardBillingProcess") BillingProcess billingProcess) {
        this.billingProcess = billingProcess;
    }

    @PatchMapping("/start")
    public ResponseEntity<?> startBilling(
            @RequestParam(name = "command", defaultValue = "generate") String command) {
        BillingStatus status = this.billingProcess.startBilling(command);
        Map<String, String> json = Map.of("billing_status", status.name());
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

    @GetMapping("/information")
    public ResponseEntity<?> getBillingProcessInformation() {
        BillingProcessInformation information = this.billingProcess.getInformation();
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(information);
    }

    @GetMapping("/current-data")
    public ResponseEntity<?> getCurrentData() {
        Object data = this.billingProcess.getCurrentData();
        Map<String, Object> json = Map.of("data", data);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

    @GetMapping("/join-to-process")
    public ResponseEntity<?> joinToProcess(@RequestParam(name = "timeout", defaultValue = "0") Long timeout) {
        try {
            Object data = this.billingProcess.joinToProcess(timeout);
            Map<String, Object> json = Map.of("data", data);
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
