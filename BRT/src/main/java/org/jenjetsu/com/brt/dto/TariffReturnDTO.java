package org.jenjetsu.com.brt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record TariffReturnDTO(
                                String name,
                                String description,
                                @JsonProperty("base_cost")
                                Float baseCost,
                                List<CallOptionCardReturnDTO> cards
                              ) {
}
