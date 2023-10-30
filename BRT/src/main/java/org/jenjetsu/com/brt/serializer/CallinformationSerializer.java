package org.jenjetsu.com.brt.serializer;

import org.jenjetsu.com.core.entity.CallInformation;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.function.Function;

import static java.lang.String.format;

@Component
public class CallinformationSerializer implements Function<CallInformation, String> {

    private final DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * <h2>callInformation serializer</h2>
     * <p>Serialize call information into string fo HRS</p>
     * <p>Output example:</p>
     * <p><code>"- call_type call_to start_calling_time end_calling_time"</code></p>
     * <p>Where:</p>
     * <p><b># call_type</b> - type of call, INPUT(0), OUTPUT(1) - write as byte</p>
     * <p><b># call_to</b> - number of listener - write as long</p>
     * <p><b># start_calling_time</b> - date and time when call started
     *   - write as format yyyyMMddHHmmss</p>
     * <p><b># end_calling_time</b> - date and time when call ends
     *   - write as format yyyyMMddHHmmss</p>
     * @param c - input call information
     * @return String
     */
    @Override
    public String apply(CallInformation c) {
        return format(
                "- %d %d %s %s",
                c.getCallTypeCode(),
                c.getCallToNumber(),
                dateFormat.format(c.getStartCallingTime()),
                dateFormat.format(c.getEndCallingTime())
        );
    }
}
