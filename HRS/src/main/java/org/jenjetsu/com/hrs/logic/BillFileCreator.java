package org.jenjetsu.com.hrs.logic;

import org.jenjetsu.com.hrs.entity.AbonentBill;
import org.jenjetsu.com.hrs.entity.CallBillInformation;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class BillFileCreator {

    public static Resource writeBillsToFile(List<AbonentBill> abonentBillList) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Iterator<AbonentBill> billIterator = abonentBillList.listIterator();
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            while (billIterator.hasNext()) {
                AbonentBill abonentBill = billIterator.next();
                outputStream.write(String.format("%d\n", abonentBill.getPhoneNumber()).getBytes(StandardCharsets.UTF_8));
                for(CallBillInformation info : abonentBill.getCallBillInformationList()) {
                    outputStream.write(String.format("%d %d %s %s %d %f\n",info.getCallType(), info.getCallTo(),
                            format.format(info.getStartCallingDate()), format.format(info.getEndCallingDate()),
                            info.getDuration(), info.getCost()
                    ).getBytes(StandardCharsets.UTF_8));
                }
                outputStream.write("===============================================\n".getBytes());
                outputStream.write(String.format("Total sum: %f" + (billIterator.hasNext() ? "\n" : ""), abonentBill.getTotalCost()).getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ByteArrayResource(outputStream.toByteArray()) {
            @Override
            public String getFilename() {
                return UUID.randomUUID().toString()+".bill";
            }
        };
    }
}
