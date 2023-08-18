package org.jenjetsu.com.hrs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbonentBill {
    private Long phoneNumber;
    private List<CallBillInformation> callBillInformationList;
    private Double totalCost;
}
