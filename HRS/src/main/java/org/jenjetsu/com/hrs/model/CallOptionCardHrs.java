package org.jenjetsu.com.hrs.model;

import java.time.Duration;
import java.util.UUID;

public interface CallOptionCardHrs {

    public UUID getTariffId();
    public CallOptionHrs getInputOption();
    public CallOptionHrs getOutputOption();
    public Duration getSharedBuffer();
    public Byte getCardPriority();
    public Float getCardCost();
    public void setInputOption(CallOptionHrs inputOption);
    public void setOutputOption(CallOptionHrs outputOption);
    public CallOptionCardHrs clone();

}
