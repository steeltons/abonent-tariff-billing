package org.jenjetsu.com.hrs.logic;

import org.jenjetsu.com.core.entity.AbonentBill;
import org.jenjetsu.com.core.entity.CallBillInformation;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BillFileCreator {

    public Resource writeBillsToFile(List<AbonentBill> abonentBillList) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Iterator<AbonentBill> billIterator = abonentBillList.listIterator();
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            while (billIterator.hasNext()) {
                AbonentBill abonentBill = billIterator.next();
                outputStream.write(String.format("%d\n", abonentBill.getPhoneNumber()).getBytes(StandardCharsets.UTF_8));
                for(CallBillInformation info : abonentBill.getCallBillInformationList()) {
                    outputStream.write(String.format("%d %d %s %s %d %s\n",info.getCallType(), info.getCallTo(),
                            format.format(info.getStartCallingDate()), format.format(info.getEndCallingDate()),
                            info.getDuration(), String.format("%.2f", info.getCost()).replace(",",".")
                    ).getBytes(StandardCharsets.UTF_8));
                }
                outputStream.write("===============================================\n".getBytes());
                outputStream.write(String.format("Total sum: %s" + (billIterator.hasNext() ? "\n" : ""), String.format("%.2f", abonentBill.getTotalCost()).replace(",",".")).getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ByteArrayResource(outputStream.toByteArray()) {
            private final String filename = UUID.randomUUID().toString()+".bill";
            @Override
            public String getFilename() {
                return filename;
            }
        };
    }
}
