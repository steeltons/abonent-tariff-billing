package org.jenjetsu.com.hrs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tariff {
    private Integer tariffId;
    private Double basePrice;
    private Long regionCode;
}
