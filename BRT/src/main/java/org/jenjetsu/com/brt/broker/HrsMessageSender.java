package org.jenjetsu.com.brt.broker;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class HrsMessageSender {

    private final RabbitTemplate template;

    public void sendFilenameToHrs(String cdrPlusFilename) {
        log.info("Send cdr plus file {} to Hrs.", cdrPlusFilename);
        template.convertAndSend("brt-queue-listener", cdrPlusFilename);
    }
}
