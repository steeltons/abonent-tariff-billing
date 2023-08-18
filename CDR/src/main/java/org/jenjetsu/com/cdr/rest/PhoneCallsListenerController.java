package org.jenjetsu.com.cdr.rest;

import lombok.AllArgsConstructor;
import org.jenjetsu.com.core.dto.PhoneNumberListDto;
import org.jenjetsu.com.core.entity.CallInformation;
import org.jenjetsu.com.cdr.logic.*;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/calls")
@AllArgsConstructor
public class PhoneCallsListenerController {

    private final CallInfoCollector callInfoCollector;
    private final CallInfoGenerator callInfoGenerator;
    private final CallInfoCreator callsInfoCreator;
    private final CdrFileResourceGenerator resourceGenerator;
    private final PhoneNumberGenerator numberGenerator;

    @PostMapping("/generate-calls-from")
    public ResponseEntity<?> generateCalls(@RequestBody PhoneNumberListDto dto) {
        Collection<CallInformation> calls = callsInfoCreator.generateCollectionOfCalls(dto.phoneNumbers());
        Resource cdrFile = resourceGenerator.generateCdrResourceFromCalls(calls);
        return ResponseEntity.ok(cdrFile);
    }

    @GetMapping("/generate-numbers")
    public ResponseEntity<?> generateNumbers(@RequestParam(name = "count", required = false, defaultValue = "10") Integer count) {
        Collection<Long> numbers = numberGenerator.generatePhoneNumbers(count);
        return ResponseEntity.ok(numbers);
    }

    @GetMapping("/get-calls")
    public ResponseEntity<?> generateCalls(@RequestParam(value = "command", required = false, defaultValue = "collect-and-send") String command) {
        if(!command.equals("generate") && !command.equals("collect-and-send")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not correct command. Correct commands: generate, collect-and-send");
        }
        Resource cdrFile = command.equals("generate") ? callInfoGenerator.generateCallInformation() : callInfoCollector.createCdrFile();
        return ResponseEntity.ok(cdrFile);
    }
}
