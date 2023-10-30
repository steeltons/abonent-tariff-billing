package org.jenjetsu.com.brt.broker;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
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
import java.util.function.Function;


@Service
@AllArgsConstructor
public class CdrListener {

    private final MinioService minioService;
    private final Function<Resource, Map<Long, List<CallInformation>>> cdrFileParser;
    private final Function<Map<Long, List<CallInformation>>, Resource> cdrPlusResourceCreator;
    private final HrsMessageSender messageSender;


    /**
     * <h2>handleCdrFile</h2>
     * <p>Get cdr filename from CDR, convert into cdr plus file, store into s3 and send name to HRS</p>
     * @param filename - cdr filename
     */
    @RabbitListener(queues = "cdr-queue-listener")
    @SneakyThrows
    public void handleCdrFile(String filename){
        Resource cdrFile = minioService.getFile(filename, "trash");
        Map<Long, List<CallInformation>> abonentCallMap = cdrFileParser.apply(cdrFile);
        Resource cdrPlusFile = cdrPlusResourceCreator.apply(abonentCallMap);
        minioService.putFile(cdrPlusFile.getFilename(), "trash", cdrPlusFile.getInputStream());
        messageSender.sendFilenameToHrs(cdrPlusFile.getFilename());
    }

}
