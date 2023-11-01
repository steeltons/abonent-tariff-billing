package org.jenjetsu.com.hrs.model.implementation;

import lombok.*;
import org.jenjetsu.com.hrs.model.CallOptionHrs;
import org.jenjetsu.com.hrs.model.DurationShell;

import java.time.Duration;
import java.util.Objects;

@Getter @Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CallOptionImpl implements CallOptionHrs, Cloneable{

    private Long callOptionId;
    private Float minuteCost;
    private DurationShell minuteBuffer;

    @Override
    public CallOptionHrs clone() {
        return CallOptionImpl.builder()
                .callOptionId(this.callOptionId)
                .minuteCost(this.minuteCost)
                .minuteBuffer(Objects.nonNull(this.minuteBuffer) ? DurationShell.ofSeconds(this.minuteBuffer.getSeconds()) : DurationShell.INFINITY)
                .build();
    }
}
