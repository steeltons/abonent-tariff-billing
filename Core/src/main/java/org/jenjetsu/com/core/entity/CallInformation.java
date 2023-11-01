package org.jenjetsu.com.core.entity;

import lombok.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter @Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CallInformation {

    private byte callTypeCode;
    private Long phoneNumber;
    private Long callToNumber;
    private Date startCallingTime;
    private Date endCallingTime;

    public long getCallDurationInSeconds() {
        long callingTimeMillis = endCallingTime.getTime() - startCallingTime.getTime();
        return (long) Math.ceil(callingTimeMillis / 1000.0);
    }

    public static CallInformation parseFromLine(String line) throws ParseException, IllegalArgumentException {
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String[] words = line.split(" ");
        CallInformation call = new CallInformation();
        call.callTypeCode = Byte.parseByte(words[0]);
        call.phoneNumber = Long.parseLong(words[1]);
        call.callToNumber = Long.parseLong(words[2]);
        call.startCallingTime = format.parse(words[3]);
        call.endCallingTime = format.parse(words[4]);
        if(call.getCallDurationInSeconds() < 0) {
            throw new IllegalArgumentException("CallInformation parse exception. Start calling date is later than End calling date!");
        }
        return call;
    }
    @Override
    public String toString() {
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return String.format("%d %d %d %s %s", callTypeCode, phoneNumber,  callToNumber,
                format.format(startCallingTime), format.format(endCallingTime));
    }
    @Override
    public int hashCode() {
        int res = 1;
        res = res * 31 + callTypeCode;
        res = res * 31 + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        res = res * 31 + (callToNumber != null ? callToNumber.hashCode() : 0);
        res = res * 31 + (startCallingTime != null ? startCallingTime.hashCode() : 0);
        res = res * 31 + (endCallingTime != null ? endCallingTime.hashCode() : 0);
        return res;
    }
}
