package org.jenjetsu.com.hrs.broker;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class BrtMessageSender {

    private final RabbitTemplate rabbitTemplate;
    private final String BRT_QUEUE_NAME = "hrs-queue-listener";

    /**
     * <h2>sendBillFilenameToBrt</h2>
     * <p2>Send bill filename to BRT</p2>
     * @param billFilename
     */
    public void sendBillFilenameToBrt(String billFilename) {
        log.info("Send bill file {} to brt.", billFilename);
        rabbitTemplate.convertAndSend(BRT_QUEUE_NAME, billFilename);
    }
}
