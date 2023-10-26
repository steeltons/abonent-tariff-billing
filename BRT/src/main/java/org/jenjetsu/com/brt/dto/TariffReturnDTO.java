package org.jenjetsu.com.brt.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record TariffReturnDTO(
                                String name,
                                String description,
                                Float baseCost,
                                List<CallOptionReturnDTO> options
                              ) {
}
