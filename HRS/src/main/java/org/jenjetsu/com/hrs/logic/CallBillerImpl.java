package org.jenjetsu.com.hrs.logic;

import org.apache.logging.log4j.core.util.datetime.DateParser;
import org.jenjetsu.com.hrs.entity.AbonentBill;
import org.jenjetsu.com.hrs.entity.AbonentInformation;
import org.jenjetsu.com.hrs.entity.CallBillInformation;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CallBillerImpl implements CallBiller{

    private Duration incomingCallBuffer;
    private Duration outcomingCallBuffer;
    private Double incomingCallCost;
    private Double outcomingCallCost;

    @Override
    public AbonentBill billAbonent(AbonentInformation abonentInformation) {
        AbonentBill abonentBill = new AbonentBill();
        abonentBill.setPhoneNumber(79146878167l);
        abonentBill.setTotalCost(150.0);
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        List<CallBillInformation> list = new ArrayList<>();
        try {
            list.add(new CallBillInformation((byte) 0, 79146666666l, format.parse("12.12.2012 15:30:33"), format.parse("12.12.2012 15:33:33"), 180l, 50.0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        abonentBill.setCallBillInformationList(list);
        return abonentBill;
    }
}
