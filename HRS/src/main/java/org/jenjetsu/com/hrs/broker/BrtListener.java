package org.jenjetsu.com.hrs.broker;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jenjetsu.com.hrs.entity.AbonentBill;
import org.jenjetsu.com.hrs.entity.AbonentInformation;
import org.jenjetsu.com.hrs.logic.BillFileCreator;
import org.jenjetsu.com.hrs.logic.CallBiller;
import org.jenjetsu.com.hrs.logic.CdrFileParser;
import org.jenjetsu.com.hrs.service.MinioService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BrtListener {

    private final MinioService minioService;
    private final CallBiller callBiller;

    @RabbitListener(queues = "bill-abonents")
    public void receiveBrtCommand(String filename) {
        //InputStream fileInputStream = minioService.getObjectStream(filename);
        //List<AbonentInformation> abonentInformationList = CdrFileParser.parseCallInformationFromFile(fileInputStream);
        List<AbonentInformation> abonentInformationList = new ArrayList<>();
        abonentInformationList.add(new AbonentInformation());
        List<AbonentBill> abonentBillList = new ArrayList<>();
        for(AbonentInformation abonentInformation : abonentInformationList) {
            abonentBillList.add(callBiller.billAbonent(abonentInformation));
        }
        Resource billFile = BillFileCreator.writeBillsToFile(abonentBillList);
        try {
            minioService.putObject(billFile.getFilename(), billFile.getInputStream());
            log.info("Save file with name {}", billFile.getFilename());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // возврат в BRT
    }
}
