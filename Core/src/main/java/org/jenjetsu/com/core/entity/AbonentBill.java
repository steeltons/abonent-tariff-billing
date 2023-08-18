package org.jenjetsu.com.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbonentBill {
    private Long phoneNumber;
    private List<CallBillInformation> callBillInformationList;
    private Double totalCost;
}
