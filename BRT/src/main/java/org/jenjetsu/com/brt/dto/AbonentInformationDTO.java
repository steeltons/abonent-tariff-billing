package org.jenjetsu.com.brt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record AbonentInformationDTO(
                                    @JsonProperty("abonent_id")
                                    UUID abonentId,
                                    @JsonProperty("phone_number")
                                    Long phoneNumber,
                                    Float balance,
                                    TariffReturnDTO tariff,
                                    @JsonProperty("is_blocked")
                                    Boolean isBlocked,
                                    List<AbonentPayloadReturnDTO> payloads
                                   ) {


}
