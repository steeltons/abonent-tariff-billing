package org.jenjetsu.com.cdr.logic;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jenjetsu.com.cdr.database.PhoneNumberService;
import org.jenjetsu.com.core.dto.PhoneNumberListDto;
import org.jenjetsu.com.core.entity.CallInformation;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
@Slf4j
public class CallInfoGenerator {

    private final CallInfoCreator callsInfoCreator;
    private final CdrFileResourceGenerator resourceGenerator;
    private final PhoneNumberService phoneNumberService;

    /**
     * <h2>Generate call information</h2>
     * Generate call information from phone numbers that exists in database
     * @return cdr file as resource
     */
    public Resource generateCallInformation() {
        try {
            log.info("Start generate CDR file.");
            Collection<Long> phoneNumbers = phoneNumberService.getNotBlockedPhoneNumbers();
            Collection<CallInformation> calls = callsInfoCreator.generateCollectionOfCalls(phoneNumbers);
            Resource cdrFile = resourceGenerator.generateCdrResourceFromCalls(calls);
            log.info("End generate CDR file");
            return cdrFile;
        } catch (Exception e) {
            throw new RuntimeException("Impossible to generate call information", e);
        }
    }
}
