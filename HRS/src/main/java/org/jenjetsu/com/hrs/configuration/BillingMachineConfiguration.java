package org.jenjetsu.com.hrs.configuration;

import org.jenjetsu.com.hrs.biller.BillingMachine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BillingMachineConfiguration {

    @Bean
    public BillingMachine billingMachine() {
        return new BillingMachine();
    }
}
