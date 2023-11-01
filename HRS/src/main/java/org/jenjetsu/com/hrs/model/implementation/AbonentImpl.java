package org.jenjetsu.com.hrs.model.implementation;

import lombok.*;
import org.jenjetsu.com.hrs.model.AbonentHrs;
import org.jenjetsu.com.hrs.model.CallHrs;
import org.jenjetsu.com.hrs.model.TariffHrs;

import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AbonentImpl implements AbonentHrs {

    private Long phoneNumber;
    private TariffHrs tariff;
    private List<CallHrs> callList;
}
