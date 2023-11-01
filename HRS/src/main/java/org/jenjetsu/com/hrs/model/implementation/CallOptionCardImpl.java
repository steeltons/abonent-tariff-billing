package org.jenjetsu.com.hrs.model.implementation;

import lombok.*;
import org.jenjetsu.com.hrs.model.CallOptionCardHrs;
import org.jenjetsu.com.hrs.model.CallOptionHrs;
import org.jenjetsu.com.hrs.model.DurationShell;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CallOptionCardImpl implements CallOptionCardHrs, Cloneable{

    private UUID tariffId;
    private CallOptionHrs inputOption;
    private CallOptionHrs outputOption;
    private Duration sharedBuffer;
    private Byte cardPriority;
    private Float cardCost;

    @Override @SneakyThrows
    public CallOptionCardHrs clone() {
        CallOptionHrs inputOption = this.inputOption != null ? this.inputOption.clone() : null;
        CallOptionHrs outputOption = this.outputOption != null ? this.outputOption.clone() : null;
        if (this.cardPriority != null && this.cardPriority.equals((byte)0)) {
            inputOption.setMinuteBuffer(DurationShell.INFINITY); // if throw NullPointerException - it's your fault
            outputOption.setMinuteBuffer(DurationShell.INFINITY); // every tariff has input and output call options
        } else if(this.sharedBuffer != null) {
            DurationShell linkedDuration = DurationShell.ofSeconds(this.sharedBuffer.toSeconds());
            if(inputOption != null && outputOption != null) {
                inputOption.setMinuteBuffer(linkedDuration);
                outputOption.setMinuteBuffer(linkedDuration);
            }
        }
        return CallOptionCardImpl.builder()
                .tariffId(this.tariffId != null ? UUID.fromString(this.tariffId.toString()) : null)
                .inputOption(inputOption)
                .outputOption(outputOption)
                .sharedBuffer(this.sharedBuffer != null ? Duration.ofSeconds(this.sharedBuffer.toSeconds()) : null)
                .cardPriority(this.cardPriority != null ? Byte.valueOf(this.cardPriority) : null)
                .cardCost(this.cardCost != null ? Float.valueOf(this.cardCost) : null)
                .build();
    }
}
