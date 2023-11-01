package org.jenjetsu.com.hrs.broker;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jenjetsu.com.core.service.MinioService;
import org.jenjetsu.com.hrs.biller.BillingMachine;
import org.jenjetsu.com.hrs.logic.BillFileCreator;
import org.jenjetsu.com.hrs.model.AbonentHrs;
import org.jenjetsu.com.hrs.model.AbonentHrsOut;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrtListener {

    private final MinioService minioService;
    private final BillingMachine billingMachine;
    private final BrtMessageSender messageSender;
    private final Function<List<AbonentHrsOut>, Resource> fileCreator;
    private final Function<Resource, List<AbonentHrs>> cdrPlusFileParser;

    /**
     * <h2>handleCdrPlusFile</h2>
     * <p>Get cdr plus filename from BRT, bill it, save in s3 and send bill filename to BRT</p>
     * @param filename cdr plus filename
     */
    @SneakyThrows
    @RabbitListener(queues = "brt-queue-listener")
    public void handleCdrPlusFile(String filename) throws IOException{
        Resource cdrPlusFile = minioService.getFile(filename, "trash");
        List<AbonentHrs> abonentList = this.cdrPlusFileParser.apply(cdrPlusFile);
        List<AbonentHrsOut> billedAbonents = this.billingMachine.startBilling(abonentList);
        Resource billFile = fileCreator.apply(billedAbonents);
        minioService.putFile(billFile.getFilename(), "trash", billFile.getInputStream());
        messageSender.sendBillFilenameToBrt(billFile.getFilename());
    }
}
