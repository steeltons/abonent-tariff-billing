package org.jenjetsu.com.brt.logic;

import java.io.ByteArrayOutputStream;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.jenjetsu.com.brt.entity.Abonent;
import org.jenjetsu.com.brt.entity.AbonentPayload;
import org.jenjetsu.com.brt.entity.enums.CallType;
import org.jenjetsu.com.brt.service.AbonentPayloadService;
import org.jenjetsu.com.brt.service.AbonentService;
import org.jenjetsu.com.core.entity.AbonentBill;
import org.jenjetsu.com.core.service.MinioService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class AbonentBiller {

    private final Double MIN_BALANCE_TO_BLOCK = -300.0;

    private final AbonentService abonentService;
    private final AbonentPayloadService payloadService;
    private final MinioService minioService;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void billAbonents(List<AbonentBill> abonentBillList) {
        log.info("Start billing abonents.");
        long blockedAbonents = 0l;
        try {
            for(AbonentBill abonentBill: abonentBillList) {
                Abonent abonent = this.abonentService.readByPhoneNumber(abonentBill.getPhoneNumber());
                abonent.setBalance(abonent.getBalance() - abonentBill.getTotalCost().floatValue());
                if(abonent.getBalance() < MIN_BALANCE_TO_BLOCK) {
                    abonent.setIsBlocked(true);
                    blockedAbonents++;
                }
                abonentBill.getCallBillInformationList()
                    .stream()
                    .map(info -> AbonentPayload.builder()
                                        .abonent(abonent)
                                        .callType(CallType.getByCode(info.getCallType()))
                                        .callTo(info.getCallTo())
                                        .startCallingTime(Timestamp.from(info.getStartCallingDate().toInstant()))
                                        .endCallingTime(Timestamp.from(info.getEndCallingDate().toInstant()))
                                        .cost(info.getCost().floatValue())
                                        .duration(new Time(info.getDuration()))
                                        .build()
                        )
                    .forEach(this.payloadService::create);
            }
            log.info("End billing abonents");
            Resource resultFile = this.createBillingResultFile(abonentBillList, blockedAbonents);
            minioService.putFile(resultFile.getFilename(), "trash", resultFile.getInputStream());
            log.info("Information of billing in {} file.", resultFile.getFilename());
        } catch(Exception e) {
            throw new RuntimeException("Error billing abonents.", e);
        }

    }



    private Resource createBillingResultFile(List<AbonentBill> abonentBillList, long blockedAbonents) throws Exception{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Date date = new Date(System.currentTimeMillis());
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        outputStream.write(("Billing time: " + format.format(date) + "\n").getBytes());
        outputStream.write(String.format("Total billed numbers : %d\n", abonentBillList.size()).getBytes());
        outputStream.write(String.format("Total blocked numbers : %d\n", blockedAbonents).getBytes());
//        outputStream.write(String.format("Total spoked seconds : %d\n").getBytes());
//        outputStream.write(String.format("Total obtained money: %f\n").getBytes());
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
