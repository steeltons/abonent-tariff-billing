package org.jenjetsu.com.hrs.model;

import java.util.Collection;
import java.util.List;

public interface AbonentHrsOut {

    public Long getPhoneNumber();
    public void setPhoneNumber(Long phoneNumber);
    public Collection<TariffedCall> getTariffedCallList();
    public void setTariffedCallList(List<TariffedCall> tariffedCallList);
    public Float getTotalPrice();
    public void setTotalPrice(Float price);
}
