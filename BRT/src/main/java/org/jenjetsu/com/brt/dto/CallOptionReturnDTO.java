package org.jenjetsu.com.brt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.jenjetsu.com.brt.entity.enums.CallType;

@Builder
public record CallOptionReturnDTO(
                                    @JsonProperty("call_type")
                                    CallType callType,
                                    @JsonProperty("minute_cost")
                                    Float minuteCost,
                                    Short minutes,
                                    @JsonProperty("call_priority")
                                    Byte callPriority,
                                    @JsonProperty("are_call_types_linked")
                                    Boolean areCallTypesLinked,
                                    @JsonProperty("option_cost")
                                    Float optionCost
                                 ) {
}
