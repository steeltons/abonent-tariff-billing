package org.jenjetsu.com.hrs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallInformation {

    private Byte callType;
    private Long callToNumber;
    private Boolean isCallToSameProvider;
    private Date startCallingTime;
    private Date endCallingTime;
}
