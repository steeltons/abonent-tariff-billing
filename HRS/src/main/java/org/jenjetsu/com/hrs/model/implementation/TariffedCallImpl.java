package org.jenjetsu.com.hrs.model.implementation;

import lombok.*;
import org.jenjetsu.com.hrs.model.TariffedCall;

import java.time.Duration;
import java.util.Date;

@Getter @Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TariffedCallImpl implements TariffedCall {

    private Byte callType;
    private Long callTo;
    private Date startCallingTime;
    private Date endCallingTime;
    private Duration callDuration;
    private Float callCost;
}
