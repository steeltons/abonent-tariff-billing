package org.jenjetsu.com.cdr.broker;

import lombok.extern.slf4j.Slf4j;
import org.jenjetsu.com.core.entity.CallInformation;
import org.jenjetsu.com.core.service.MinioService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
@Slf4j
public class BrtListener {

    private final Supplier<Collection<CallInformation>> callInfoGenerator;
    private Supplier<Collection<CallInformation>> callInfoCollector;
    private final MinioService minioService;
    private final Function<Collection<CallInformation>, Resource> cdrCreator;
    private final BrtMessageSender messageSender;

    @Autowired(required = false)
    public BrtListener(
            @Nullable Function<Collection<CallInformation>, Resource> cdrCreator,
            @Nullable MinioService minioService,
            @Nullable BrtMessageSender messageSender,
            @Nullable @Qualifier("callInfoGenerator") Supplier<Collection<CallInformation>> callInfoGenerator) {
        this.cdrCreator = cdrCreator;
        this.minioService = minioService;
        this.messageSender = messageSender;
        this.callInfoGenerator = callInfoGenerator;
    }

    @RabbitListener(queues = "command-listener")
    public void commandListener(String command){
        try {
            if(!command.equals("generate") && !command.equals("collect-and-send")) {
                throw new RuntimeException("Billing command is not generate or collect-and-send");
            }
            Collection<CallInformation> calls;
            if(command.equals("generate")) {
                calls = this.callInfoGenerator.get();
            } else {
                if(this.callInfoCollector == null) {
                    throw new RuntimeException("CallInfoCollector is disabled");
                }
                calls = this.callInfoCollector.get();
            }
            Resource cdrFile = this.cdrCreator.apply(calls);
            minioService.putFile(cdrFile.getFilename(), "trash", cdrFile.getInputStream());
            messageSender.sendCdrNameToBrt(cdrFile.getFilename());
        } catch (Exception e) {
            this.messageSender.sendBillException(e);
        }
    }

    @Autowired(required = false)
    public void setCallInfoCollector(
            @Qualifier("callInfoCollector") Supplier<Collection<CallInformation>> callInfoCollector) {
        this.callInfoCollector = callInfoCollector;
    }
}
