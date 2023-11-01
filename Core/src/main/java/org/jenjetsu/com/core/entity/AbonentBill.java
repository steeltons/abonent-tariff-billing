package org.jenjetsu.com.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AbonentBill {
    private Long phoneNumber;
    private List<CallBillInformation> callBillInformationList;
    private Double totalCost;
}
