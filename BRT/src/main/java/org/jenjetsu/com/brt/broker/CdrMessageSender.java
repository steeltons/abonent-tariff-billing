package org.jenjetsu.com.brt.broker;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CdrMessageSender {

    private final RabbitTemplate rabbitTemplate;
    private final String CDR_QUEUE_NAME = "command-listener";

    /**
     * <h2>sendCdrCommand</h2>
     * <p>Send command "<code>generate</code>" or "<code>collect-and-send</code>" to CDR to get collected calls</p>
     * @param command - command for CDR
     */
    public void sendCdrCommand(String command) {
        log.info("Send command {} to CDR.", command);
        rabbitTemplate.convertAndSend(CDR_QUEUE_NAME, command);
    }
}

