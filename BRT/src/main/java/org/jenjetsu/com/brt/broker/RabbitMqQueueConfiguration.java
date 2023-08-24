package org.jenjetsu.com.brt.broker;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqQueueConfiguration {

    @Bean
    public Queue cdrQueueListener() {
        return new Queue("cdr-queue-listener");
    }

    @Bean
    public Queue hrsQueueListener() {
        return new Queue("hrs-queue-listener");
    }
}
