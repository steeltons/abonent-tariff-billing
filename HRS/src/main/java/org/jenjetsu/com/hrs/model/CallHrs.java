package org.jenjetsu.com.hrs.model;

import java.util.Date;

public interface CallHrs {

    public Byte INPUT_CALL_TYPE = (byte) 1;
    public Byte OUTPUT_CALL_TYPE = (byte) 0;

    public Byte getCallType();
    public Long getCallTo();
    public Date getStartCallingTime();
    public Date getEndCallingTime();
    public Long getCallDurationInSeconds();
}
