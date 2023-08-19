package org.jenjetsu.com.hrs.broker;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jenjetsu.com.core.entity.AbonentBill;
import org.jenjetsu.com.core.service.MinioService;
import org.jenjetsu.com.hrs.entity.AbonentInformation;
import org.jenjetsu.com.hrs.logic.BillFileCreator;
import org.jenjetsu.com.hrs.logic.CallBiller;
import org.jenjetsu.com.hrs.logic.CdrPlusFileParser;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BrtListener {

    private final MinioService minioService;
    private final CallBiller callBiller;
    private final BillFileCreator fileCreator;
    private final CdrPlusFileParser cdrPlusFileParser;
    private final BrtMessageSender messageSender;

    @RabbitListener(queues = "brt-queue-listener")
    public void handleCdrPlusFile(String filename) throws IOException{
        Resource cdrPlusFile = minioService.getFile(filename, "trash");
        List<AbonentInformation> abonentInformationList = cdrPlusFileParser.parseCdrPlusFile(cdrPlusFile);
        List<AbonentBill> abonentBillList = new ArrayList<>();
        for(AbonentInformation abonentInformation : abonentInformationList) {
            abonentBillList.add(callBiller.billAbonent(abonentInformation));
        }
        Resource billFile = fileCreator.writeBillsToFile(abonentBillList);
        minioService.putFile(billFile.getFilename(), "trash", billFile.getInputStream());
        messageSender.sendBillFilenameToBrt(billFile.getFilename());
    }
}
