package org.jenjetsu.com.brt.dto;

import org.jenjetsu.com.brt.entity.AbonentPayload;

import java.util.List;

public record AbonentPayloadListDTO(Long phoneNumber,
                                    List<AbonentPayloadReturnDTO> payloads) {
}
