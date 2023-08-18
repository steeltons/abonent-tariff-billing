package org.jenjetsu.com.brt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Time;
import java.util.Date;

@Data
@AllArgsConstructor
public class AbonentPayloadDto {

    private final CallTypeEnum callType;
    private final Long myNumber;
    private final Long callTo;
    private final Date startCallingDate;
    private final Date endCallingDate;
    private final Time duration;
    private final Float cost;

    public AbonentPayloadDto(Byte callType, Long myNumber, Long callTo, Date startCallingTime,
                             Date endCallingTime, Time duration, Float cost) {
        this.callType = parseCallTypeByte(callType);
        this.myNumber = myNumber;
        this.callTo = callTo;
        this.startCallingDate = startCallingTime;
        this.endCallingDate = endCallingTime;
        this.duration = duration;
        this.cost = cost;
    }

    public enum CallTypeEnum {
        INCOMING_CALL, OUTCOMING_CALL;
    };

    private CallTypeEnum parseCallTypeByte(Byte callTypeByte) {
        return switch (callTypeByte) {
            case 0 -> CallTypeEnum.INCOMING_CALL;
            case 1 -> CallTypeEnum.OUTCOMING_CALL;
            default -> throw new IllegalArgumentException("No call type");
        };
    }
}
