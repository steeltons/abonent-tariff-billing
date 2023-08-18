package org.jenjetsu.com.hrs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TariffOption {
    private Byte callType;
    private Double minuteCost;
    private Long optionBuffer;
    private Double optionPrice;
}
