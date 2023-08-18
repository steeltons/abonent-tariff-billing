package org.jenjetsu.com.hrs.logic;

import org.jenjetsu.com.hrs.entity.AbonentBill;
import org.jenjetsu.com.hrs.entity.AbonentInformation;

import java.util.List;

public interface CallBiller {

    public AbonentBill billAbonent(AbonentInformation abonentInformation);
}
