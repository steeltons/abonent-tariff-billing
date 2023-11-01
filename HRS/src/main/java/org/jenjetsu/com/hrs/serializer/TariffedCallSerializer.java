package org.jenjetsu.com.hrs.serializer;

import org.jenjetsu.com.hrs.model.TariffedCall;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.function.Function;

import static java.lang.String.format;

@Component
public class TariffedCallSerializer implements Function<TariffedCall, String> {

    private final DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private final DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.US);
    private final DecimalFormat costFormat = new DecimalFormat("#####.##", formatSymbols);

    /**
     * <h2>serializeTariffedCall</h2>
     * <p>Serialize tariffed call as string</p>
     * <p>Output example:</p>
     * <code>"- call_type call_to start_calling_time end_calling_time call_duration call_cost"</code>
     * <p>Where:</p>
     * <p><b># call_type</b> - type of call (0 - Input, 1 - Output) - write as byte</p>
     * <p><b># call_to</b> - caller or listener - write as long</p>
     * <p><b># start_calling_time</b> - date and time of call start - write as string</p>
     * <p><b># end_calling_time</b> - date and time of call end - write as string</p>
     * <p><b># call_duration</b> - duration of call in seconds - write as long</p>
     * <p><b># call_cost</b> - cost of call - write as float</p>
     * @param call input tariffed call
     * @return tariffed call information
     */
    public String apply(TariffedCall call) {
        return format("- %d %d %s %s %d %s",
                        call.getCallType(),
                        call.getCallTo(),
                        dateFormat.format(call.getStartCallingTime()),
                        dateFormat.format(call.getEndCallingTime()),
                        call.getCallDuration().getSeconds(),
                        costFormat.format(call.getCallCost())
                        );
    }
}
