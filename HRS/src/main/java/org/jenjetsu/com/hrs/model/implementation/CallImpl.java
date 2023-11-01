package org.jenjetsu.com.hrs.model.implementation;

import lombok.*;
import org.jenjetsu.com.hrs.model.CallHrs;

import java.util.Date;

@Getter @Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CallImpl implements CallHrs {

    private Byte callType;
    private Long callTo;
    private Date startCallingTime;
    private Date endCallingTime;

    public Long getCallDurationInSeconds() {
        return (endCallingTime.getTime() - startCallingTime.getTime()) / 1000;
    }
}
