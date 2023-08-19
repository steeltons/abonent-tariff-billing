package org.jenjetsu.com.hrs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tariff {

    private Integer tariffId;
    private Double basePrice;
    private Long regionCode;
    private List<TariffOption> tariffOptionList;

}
