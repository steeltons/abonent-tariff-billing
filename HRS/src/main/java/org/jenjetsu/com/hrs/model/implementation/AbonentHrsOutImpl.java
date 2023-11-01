package org.jenjetsu.com.hrs.model.implementation;


import lombok.*;
import org.jenjetsu.com.hrs.model.AbonentHrsOut;
import org.jenjetsu.com.hrs.model.TariffedCall;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AbonentHrsOutImpl implements AbonentHrsOut {

    private Long phoneNumber;
    private List<TariffedCall> tariffedCallList;
    private Float totalPrice;
}
