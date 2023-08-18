package org.jenjetsu.com.brt.broker;

import lombok.AllArgsConstructor;
import org.jenjetsu.com.brt.logic.AbonentBiller;
import org.jenjetsu.com.brt.logic.BillFileParser;
import org.jenjetsu.com.core.entity.AbonentBill;
import org.jenjetsu.com.core.service.MinioService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class HrsListener {

    private final BillFileParser billFileParser;
    private final MinioService minioService;
    private final AbonentBiller abonentBiller;

    @RabbitListener(queues = "hrs-queue-listener")
    public void handleBillFile(String filename) {
        Resource billFile = minioService.getFile(filename, "trash");
        List<AbonentBill> abonentBillList = billFileParser.parseBillFile(billFile);
        abonentBiller.billAbonents(abonentBillList);
    }
}
