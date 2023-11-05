package org.jenjetsu.com.cdr.serializer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.function.Function;

import org.jenjetsu.com.core.entity.CallInformation;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public class CallInformationSerializer implements Function<CallInformation, String>{
    
    private final DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    
    /**
     * <h2>serializeCallInformation</h2>
     * <p>Serialize CallInformation into formatted string</p>
     * <p>Output example:</p>
     * <code>"call_type phone_number call_to start_calling_time end_calling_time"</code>
     * <p>Where:</p>
     * <p><b>call_type</b> - type of call (0 - output, 1 - input) - write as Byte</p>
     * <p><b>phone_number</b> - phone of caller - write as Long</p>
     * <p><b>call_to</b> - phone number of listener - write as Long</p>
     * <p><b>start_calling_time</b> - date and time of call start 
     * - write as date format: yyyyMMddHHmmss</p>
     * <p><b>end_calling_time</b> - date and time of call end
     * - write as date format: yyyyMMddHHmmss</p>
     * @param call information of call
     * @return String - formatted string
     */
    public String apply(CallInformation call) {
        return format("%d %d %d %s %s",
            call.getCallTypeCode(), call.getPhoneNumber(), call.getCallToNumber(),
            this.dateFormat.format(call.getStartCallingTime()),
            this.dateFormat.format(call.getEndCallingTime())
        );
    }
}
