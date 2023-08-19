package org.jenjetsu.com.hrs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HrsCallInformation {

    private Byte callType;
    private Long callTo;
    private Boolean isSameOperator;
    private Date startCallingTime;
    private Date endCallingTime;

    public long getCallDurationInSeconds() {
        long callingTimeMillis = endCallingTime.getTime() - startCallingTime.getTime();
        return (long) Math.ceil(callingTimeMillis / 1000.0);
    }
}
