package org.jenjetsu.com.brt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.jenjetsu.com.brt.entity.enums.CallType;

@Builder
public record CallOptionReturnDTO(
        @JsonProperty("minute_cost") Float minuteCost,
        @JsonProperty("minute_buffer") Short minuteBuffer) {
}
