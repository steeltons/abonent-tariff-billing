package org.jenjetsu.com.brt.logic;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jenjetsu.com.brt.entity.Abonent;
import org.jenjetsu.com.brt.entity.AbonentPayload;
import org.jenjetsu.com.brt.repository.AbonentPayloadRepository;
import org.jenjetsu.com.brt.service.AbonentService;
import org.jenjetsu.com.core.entity.AbonentBill;
import org.jenjetsu.com.core.entity.CallBillInformation;
import org.jenjetsu.com.core.service.MinioService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AbonentBiller {

    private final Double MIN_BALANCE_TO_BLOCK = -100.0;

    private final AbonentService abonentService;
    private final AbonentPayloadRepository abonentPayloadRep;
    private final MinioService minioService;
    private final TransactionTemplate transactionTemplate;

    public void billAbonents(List<AbonentBill> abonentBillList) {
        log.info("Start billing abonents.");
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
        transactionTemplate.executeWithoutResult((status) -> {
            long blockedAbonents = 0l;
            try {
                for(AbonentBill abonentBill : abonentBillList) {
                    Abonent abonent = abonentService.getByPhoneNumber(abonentBill.getPhoneNumber());
                    abonent.decreaseBalance(abonentBill.getTotalCost());
                    if(abonent.getBalance() < MIN_BALANCE_TO_BLOCK) {
                        abonent.setIsBlocked(true);
                        blockedAbonents++;
                    }
                    for(CallBillInformation callBillInformation : abonentBill.getCallBillInformationList()) {
                        AbonentPayload payload = new AbonentPayload();
                        payload.setAbonent(abonent);
                        payload.setCallType(callBillInformation.getCallType());
                        payload.setCallTo(callBillInformation.getCallTo());
                        payload.setStartCallingTime(new java.sql.Date(callBillInformation.getStartCallingDate().getTime()));
                        payload.setEndCallingTime(new java.sql.Date(callBillInformation.getEndCallingDate().getTime()));
                        payload.setDuration(new java.sql.Time(callBillInformation.getDuration() * 1000));
                        payload.setCost(callBillInformation.getCost().floatValue());
                        abonentPayloadRep.save(payload);
                    }
                }
                log.info("End billing abonents");
                Resource resultFile = createBillingResultFile(abonentBillList, blockedAbonents);
                minioService.putFile(resultFile.getFilename(), "trash", resultFile.getInputStream());
                log.info("Information of billing in {} file.", resultFile.getFilename());
            } catch (Exception e) {
                throw new RuntimeException("Error billing abonents.", e);
            }
        });
    }

    private Resource createBillingResultFile(List<AbonentBill> abonentBillList, long blockedAbonents) throws Exception{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Date date = new Date(System.currentTimeMillis());
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        outputStream.write(("Billing time: " + format.format(date) + "\n").getBytes());
        outputStream.write(String.format("Total billed numbers : %d\n").getBytes());
        outputStream.write(String.format("Total blocked numbers : %d\n").getBytes());
        outputStream.write(String.format("Total spoked seconds : %d\n").getBytes());
        outputStream.write(String.format("Total obtained money: %f\n").getBytes());
        format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        DateFormat finalFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return new ByteArrayResource(outputStream.toByteArray()) {
            private final String filename = "bill_result_" + finalFormat.format(date) + ".txt";
            @Override
            public String getFilename() {
                return filename;
            }
        };
    }
}
