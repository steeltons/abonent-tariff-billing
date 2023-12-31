package org.jenjetsu.com.cdr.configuration;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jenjetsu.com.cdr.broker.BrtListener;
import org.jenjetsu.com.cdr.database.PhoneNumberService;
import org.jenjetsu.com.core.entity.CallInformation;
import org.jenjetsu.com.cdr.logic.CallInfoCollector;
import org.jenjetsu.com.cdr.logic.CallInfoCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

@Configuration
@Slf4j
@AllArgsConstructor
@ConditionalOnProperty(
        value = "spring.application.enable-auto-collect",
        matchIfMissing = false
)
public class CallReceiverTask {

    private final CallInfoCreator callsInfoCreator;
    private volatile List<Long> phoneNumbersDatabase = new ArrayList<>();
    private BrtListener commandListener;
    private final PhoneNumberService phoneNumberService;

    @Bean("callInfoCollector")
    @Scope("singleton")
    public Supplier<Collection<CallInformation>> callInfoCollector() {
        return new CallInfoCollector();
    }

    @Scheduled(initialDelay = 1000l, fixedRate = 20000l)
    public void generateRandomCalls(CallInfoCollector collector) {
        updateLocalDatabase();
        Collection<CallInformation> calls = callsInfoCreator.generateCollectionOfCalls(getRandomNumbersFromDatabase(8));
        collector.addNewCalls(calls);
        if(collector.getAllCalls().size() > 1000) {
            commandListener.commandListener("collect-and-send");
        }
    }

    @PostConstruct
    public void init() {
        log.info("CallReceiverTask: initialization");
    }

    private void updateLocalDatabase() {
        phoneNumbersDatabase = new ArrayList<>(phoneNumberService.getNotBlockedPhoneNumbers());
    }

    private Collection<Long> getRandomNumbersFromDatabase(int count) {
        Collection<Long> numbers = new ArrayList<>();
        Random random = new Random();
        while (numbers.size() < count) {
            numbers.add(phoneNumbersDatabase.get(random.nextInt(0, phoneNumbersDatabase.size() - 1)));
        }
        return numbers;
     }

     @PreDestroy
     private void preDestroy() {
        log.info("CallReceiveTask: destroy");
     }
}
