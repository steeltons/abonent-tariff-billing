package org.jenjetsu.com.brt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jenjetsu.com.brt.entity.AbonentPayload;
import org.jenjetsu.com.brt.entity.Tariff;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbonentInformationDto {

    private Long myId;
    private Long phoneNumber;
    private Double balance;
    private Tariff currentTariff;
    private List<AbonentPayload> myPayloads;
}
