package org.jenjetsu.com.brt.serializer;

import org.jenjetsu.com.brt.entity.CallOption;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.function.Function;

import static java.lang.String.format;

@Component
public class CallOptionSerializer implements Function<CallOption, String> {

    private final DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.US);
    private final DecimalFormat minuteCostFormat = new DecimalFormat("##.##", formatSymbols);

    /**
     * <h2>callOptionSerializer</h2>
     * <p>Convert call option to formatted string</p>
     * <p>Output example:</p>
     * <code>"call_option_id minute_cost minute_buffer"</code>
     * <p>Where:</p>
     * <p><b># call_option_id</b> - id of call option - write as long</p>
     * <p><b># minute_cost</b> - cost per minute - write as float</p>
     * <p><b># minute_buffer</b> - amount of minutes, that abonent can spoke for option price - write as short</p>
     * @param c - input call option
     * @return String
     */
    public String apply(CallOption c) {
        return format("%d %s %d",
                    c.getCallOptionId(),
                    minuteCostFormat.format(c.getMinuteCost()),
                    c.getMinuteBuffer()
                );
    }
}
