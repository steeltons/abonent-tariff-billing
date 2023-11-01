package org.jenjetsu.com.hrs.model;

import java.time.Duration;
import java.util.Date;

public interface TariffedCall {

    public Byte getCallType();
    public Long getCallTo();
    public Date getStartCallingTime();
    public Date getEndCallingTime();
    public Duration getCallDuration();
    public Float getCallCost();
}
