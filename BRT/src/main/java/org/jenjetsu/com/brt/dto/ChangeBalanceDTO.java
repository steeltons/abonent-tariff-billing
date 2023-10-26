package org.jenjetsu.com.brt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ChangeBalanceDTO(
                                @JsonProperty("phone_number")
                                Long phoneNumber,
                                Float sum,
                                @JsonProperty("new_balance")
                                Float newBalance
                              ) {
}
