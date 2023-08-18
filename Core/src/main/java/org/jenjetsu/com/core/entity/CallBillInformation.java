package org.jenjetsu.com.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallBillInformation {

    private Byte callType;
    private Long callTo;
    private Date startCallingDate;
    private Date endCallingDate;
    private Long duration;
    private Double cost;

    public static CallBillInformation parseFromLine(String line) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String[] words = line.split(" ");
        CallBillInformation billInformation = new CallBillInformation();
        billInformation.callType = Byte.parseByte(words[0]);
        billInformation.callTo = Long.parseLong(words[1]);
        billInformation.startCallingDate = format.parse(words[2]);
        billInformation.endCallingDate = format.parse(words[3]);
        billInformation.duration = Long.parseLong(words[4]);
        billInformation.cost = Double.parseDouble(words[5]);
        return billInformation;
    }
}
