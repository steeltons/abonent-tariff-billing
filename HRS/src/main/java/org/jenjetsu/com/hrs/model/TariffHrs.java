package org.jenjetsu.com.hrs.model;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface TariffHrs {

    public UUID getTariffId();
    public void setTariffId(UUID id);
    public Float getBaseCost();
    public void setBaseCost(Float cost);
    public List<CallOptionCardHrs> getCallOptionCardList();
    public void setCallOptionCardList(List<CallOptionCardHrs> list);
    public TariffHrs clone();
}
