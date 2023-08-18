package org.jenjetsu.com.hrs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
