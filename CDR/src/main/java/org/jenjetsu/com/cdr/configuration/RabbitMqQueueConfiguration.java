package org.jenjetsu.com.cdr.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqQueueConfiguration {

    @Bean
    public Queue commandListenerQueue() {
        return new Queue("command-listener");
    }

    @Bean
    public Queue brtExceptionQueue() {
        return new Queue("brt-billing-exception-queue");
    }
}

