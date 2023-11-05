package org.jenjetsu.com.brt.broker;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jenjetsu.com.brt.billing.BillingProcess;
import org.jenjetsu.com.brt.billing.BillingStatus;
import org.jenjetsu.com.brt.dto.AbonentBillingResultDTO;
import org.jenjetsu.com.brt.exception.BillingException;
import org.jenjetsu.com.brt.logic.AbonentBiller;
import org.jenjetsu.com.core.entity.AbonentBill;
import org.jenjetsu.com.core.service.MinioService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
@AllArgsConstructor
@Slf4j
public class HrsListener {

    private final Function<Resource, List<AbonentBill>> billFileParser;
    private final MinioService minioService;
    private final AbonentBiller abonentBiller;
//    private final BillingProcess billingProcess;
    private final BillingProcess<List<AbonentBillingResultDTO>> billingProcess;

    /**
     * <h2>handleBillFile</h2>
     * <p>Get bill filename from HRS and finally start billing abonents in database</p>
     * @param filename - bill filename
     */
    @RabbitListener(queues = "hrs-queue-listener")
    public void handleBillFile(String filename) {
        try {
            billingProcess.updateStatus(BillingStatus.BRT_BILLING);
            Resource billFile = minioService.getFile(filename, "trash");
            List<AbonentBill> abonentBillList = billFileParser.apply(billFile);
            List<AbonentBillingResultDTO> result = abonentBiller.billAbonents(abonentBillList);
            billingProcess.setCurrentData(result);
            log.info("End billing abonents, call response");
        } catch (Exception e) {
            billingProcess.setThrownException(new BillingException(e.getMessage()));
        }
    }
}
