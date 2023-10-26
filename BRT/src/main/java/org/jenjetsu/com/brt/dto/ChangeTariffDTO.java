package org.jenjetsu.com.brt.dto;

import java.util.UUID;

public record ChangeTariffDTO(
                                Long phoneNumber,
                                UUID oldTariffId,
                                UUID newTariffId) {
}
