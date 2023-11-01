package org.jenjetsu.com.hrs.model;

import java.util.Collection;
import java.util.List;

public interface AbonentHrs {

    public Long getPhoneNumber();
    public TariffHrs getTariff();
    public List<CallHrs> getCallList();
    public void setCallList(List<CallHrs> callList);
    public void setTariff(TariffHrs tariff);

}
