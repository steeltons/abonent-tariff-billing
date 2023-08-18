package org.jenjetsu.com.brt.dto;

public record ChangeTariffDto(Long phoneNumber, Integer oldTariffId, Integer newTariffId) {
}
