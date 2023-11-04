package org.jenjetsu.com.brt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record AbonentBillingResultDTO(
        @JsonProperty("abonent_id") UUID abonentId,
        @JsonProperty("phone_number") Long phoneNumber,
        @JsonProperty("balance_before") Float balanceBefore,
        @JsonProperty("balance_after") Float balanceAfter,
        Boolean active,
        @JsonProperty("billed_calls") List<AbonentPayloadReturnDTO> billedCalls) {
}
