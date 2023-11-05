package org.jenjetsu.com.cdr.broker;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class BrtMessageSender {

    private final RabbitTemplate rabbitTemplate;
    private final String BRT_CDR_QUEUE_NAME = "cdr-queue-listener";
    private final String BRT_EXCEPTION_QUEUE_NAME = "brt-billing-exception-queue";

    public void sendCdrNameToBrt(String cdrName) {
        log.info("Send file with name \"{}\" to BRT.", cdrName);
        rabbitTemplate.convertAndSend(BRT_CDR_QUEUE_NAME, cdrName);
    }

    public void sendBillException(Exception e) {
        log.error("CDR collection phones ends with exception {}", e.getMessage());
        String exceptionMessage = "CDR throw exception. Exception message: " + e.getMessage();
        this.rabbitTemplate.convertAndSend(this.BRT_EXCEPTION_QUEUE_NAME, exceptionMessage);
    }
}
