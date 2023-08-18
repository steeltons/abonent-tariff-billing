package org.jenjetsu.com.brt.broker;

import lombok.AllArgsConstructor;
import org.jenjetsu.com.brt.entity.Abonent;
import org.jenjetsu.com.brt.logic.CdrFileParser;
import org.jenjetsu.com.brt.logic.CdrPlusResourceCreator;
import org.jenjetsu.com.core.entity.CallInformation;
import org.jenjetsu.com.core.service.MinioService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@Service
@AllArgsConstructor
public class CdrListener {

    private final MinioService minioService;
    private final CdrFileParser cdrFileParser;
    private final CdrPlusResourceCreator cdrPlusResourceCreator;
    private final HrsMessageSender messageSender;


    @RabbitListener(queues = "cdr-queue-listener")
    public void handleCdrFile(String filename) throws IOException {
        Resource cdrFile = minioService.getFile(filename, "trash");
        Map<Long, List<CallInformation>> abonentCallMap = cdrFileParser.parseCdrFileToAbonentCalls(cdrFile);
        Resource cdrPlusFile = cdrPlusResourceCreator.convertToCdrPlusFile(abonentCallMap);
        minioService.putFile(cdrPlusFile.getFilename(), "trash", cdrPlusFile.getInputStream());
        messageSender.sendFilenameToHrs(cdrPlusFile.getFilename());
    }

}
