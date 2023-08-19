package org.jenjetsu.com.hrs.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqQueueConfiguration {

    @Bean
    public Queue brtQueueListener() {
        return new Queue("brt-queue-listener");
    }
}
