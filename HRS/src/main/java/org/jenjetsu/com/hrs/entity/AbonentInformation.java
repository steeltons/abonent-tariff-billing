package org.jenjetsu.com.hrs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbonentInformation {

    private Long phoneNumber;
    private Tariff tariff;
    private List<TariffOption> tariffOptions;
    private List<CallInformation> callInformations;
}

