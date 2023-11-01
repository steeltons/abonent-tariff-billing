package org.jenjetsu.com.hrs.model;

import java.time.Duration;
import java.util.Date;

public interface CallOptionHrs extends Cloneable{

    public Long getCallOptionId();
    public Float getMinuteCost();
    public DurationShell getMinuteBuffer();
    public CallOptionHrs clone();
    public void setMinuteBuffer(DurationShell buffer);
}
