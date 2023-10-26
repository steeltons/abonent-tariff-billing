package org.jenjetsu.com.brt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.jenjetsu.com.brt.entity.enums.CallType;

import java.sql.Time;
import java.time.Instant;

@Builder
public record AbonentPayloadReturnDTO(
                                        @JsonProperty("call_to")
                                        Long callTo,
                                        @JsonProperty("call_type")
                                        CallType callType,
                                        Float cost,
                                        @JsonProperty("start_calling_time")
                                        Instant startCallingTime,
                                        @JsonProperty("end_calling_time")
                                        Instant endCallingTime,
                                        Time duration
                                     ) {
}
