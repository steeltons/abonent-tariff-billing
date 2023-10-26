package org.jenjetsu.com.brt.dto;

import java.util.UUID;

public record RawAbonentDTO(
                            Long id, 
                            Long phoneNumber, 
                            Float balance,
                            UUID tariffId
                           ) {
}
