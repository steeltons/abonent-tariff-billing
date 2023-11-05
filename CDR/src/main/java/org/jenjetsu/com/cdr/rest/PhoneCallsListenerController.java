package org.jenjetsu.com.cdr.rest;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jenjetsu.com.core.dto.PhoneNumberListDto;
import org.jenjetsu.com.core.entity.CallInformation;
import org.jenjetsu.com.cdr.logic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.function.Function;

@RestController
@RequestMapping("/calls")
@RequiredArgsConstructor
public class PhoneCallsListenerController {

    private CallInfoCollector callInfoCollector;
    private final CallInfoGenerator callInfoGenerator;
    private final CallInfoCreator callsInfoCreator;
    private final Function<Collection<CallInformation>, Resource> cdrCreator;
    private final PhoneNumberGenerator numberGenerator;


    @PostMapping("/generate-calls-from")
    public ResponseEntity<?> generateCalls(@RequestBody PhoneNumberListDto dto) {
        Collection<CallInformation> calls = callsInfoCreator.generateCollectionOfCalls(dto.phoneNumbers());
        Resource cdrFile = cdrCreator.apply(calls);
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
        Collection<CallInformation> calls;
        if(command.equals("generate")) {
            calls = callInfoGenerator.get();
        } else {
            if(this.callInfoCollector == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("CallInfoCollector is disabled");
            }
            calls = this.callInfoCollector.get();
        }
        Resource cdrFile = this.cdrCreator.apply(calls);
        return ResponseEntity.status(HttpStatus.CREATED).body(cdrFile);
    }

    @Autowired(required = false)
    public void setCallInfoCollector(CallInfoCollector callInfoCollector) {
        this.callInfoCollector = callInfoCollector;
    }
}
