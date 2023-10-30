package org.jenjetsu.com.brt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CallOptionCardReturnDTO(
        @JsonProperty("card_id") UUID cardId,
        @JsonProperty("input_option") CallOptionReturnDTO inputOption,
        @JsonProperty("output_property") CallOptionReturnDTO outputOption,
        @JsonProperty("shared_buffer") Short sharedBuffer,
        @JsonProperty("card_priority") Byte cardPriority,
        @JsonProperty("card_cost") Float cardCost) {
}
