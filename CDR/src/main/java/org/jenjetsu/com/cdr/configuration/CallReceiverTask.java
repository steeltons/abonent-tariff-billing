package org.jenjetsu.com.cdr.configuration;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jenjetsu.com.cdr.broker.BrtListener;
import org.jenjetsu.com.core.dto.PhoneNumberListDto;
import org.jenjetsu.com.core.entity.CallInformation;
import org.jenjetsu.com.cdr.logic.CallInfoCollector;
import org.jenjetsu.com.cdr.logic.CallInfoCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

@Component
@Slf4j
@AllArgsConstructor
@ConditionalOnProperty(
        value = "spring.application.enable-auto-collect",
        matchIfMissing = false
)
public class CallReceiverTask {

    static {
        log.info("ENABLE AUTO COLLECT OF CALLS");
    }

    private final CallInfoCollector callInfoCollector;
    private final CallInfoCreator callsInfoCreator;
    private volatile List<Long> phoneNumbersDatabase = new ArrayList<>();
    private BrtListener commandListener;
    private final RestTemplate restTemplate;

    @Scheduled(initialDelay = 1000l, fixedRate = 20000l)
    public void generateRandomCalls() {
        updateLocalDatabase();
        Collection<CallInformation> calls = callsInfoCreator.generateCollectionOfCalls(getRandomNumbersFromDatabase(8));
        callInfoCollector.addNewCalls(calls);
        if(callInfoCollector.getAllCalls().size() > 1000) {
            commandListener.commandListener("collect-and-send");
        }
    }

    private void updateLocalDatabase() {
        PhoneNumberListDto dto = restTemplate.getForObject("http://BRT/api/v1/abonent/get-not-blocked", PhoneNumberListDto.class);
        phoneNumbersDatabase = dto.phoneNumbers();
    }

    private Collection<Long> getRandomNumbersFromDatabase(int count) {
        Collection<Long> numbers = new ArrayList<>();
        Random random = new Random();
        while (numbers.size() < count) {
            numbers.add(phoneNumbersDatabase.get(random.nextInt(0, phoneNumbersDatabase.size() - 1)));
        }
        return numbers;
     }
}
