package org.jenjetsu.com.cdr.broker;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jenjetsu.com.cdr.logic.CallInfoCollector;
import org.jenjetsu.com.cdr.logic.CallInfoGenerator;
import org.jenjetsu.com.core.service.MinioService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
@Slf4j
public class BrtListener {

    private final CallInfoGenerator callInfoGenerator;
    private final CallInfoCollector callInfoCollector;
    private final MinioService minioService;
    private final BrtMessageSender messageSender;

    @RabbitListener(queues = "command-listener")
    public void commandListener(String command){
        if(!command.equals("generate") && !command.equals("collect-and-send")) return;
        Resource cdrFile =  command.equals("generate") ? generateCalls() : convertCalls();
        try {
            minioService.putFile(cdrFile.getFilename(), "trash", cdrFile.getInputStream());
            messageSender.sendCdrNameToBrt(cdrFile.getFilename());
        } catch (IOException e) {
            log.error("Error to save cdr file in S3 storage. Error message: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Resource generateCalls() {
        return callInfoGenerator.generateCallInformation();
    }

    private Resource convertCalls() {
        return callInfoCollector.createCdrFile();
    }

}
