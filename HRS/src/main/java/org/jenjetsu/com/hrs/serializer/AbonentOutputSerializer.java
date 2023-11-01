package org.jenjetsu.com.hrs.serializer;

import org.jenjetsu.com.hrs.model.AbonentHrsOut;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.function.Function;

import static java.lang.String.format;

@Component
public class AbonentOutputSerializer implements Function<AbonentHrsOut, String> {

    private final DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.US);
    private final DecimalFormat totalPriceFormat = new DecimalFormat("#####.##", formatSymbols);

    /**
     * <h2>serializeOutputAbonent</h2>
     * <p>Serialize output abonent information as string</p>
     * <p>Output example:</p>
     * <code>"phone_number total_cost"</code>
     * <p>Where:</p>
     * <p><b># phone_number</b> - abonent phone number - write as long</p>
     * <p><b># total_cost</b> - abonent bill price for all calls plus tariff - write as float</p>
     * @param out output abonent to serialize
     * @return output abonent information
     */
    public String apply(AbonentHrsOut out) {
        return format("%d %s",
                    out.getPhoneNumber(),
                    totalPriceFormat.format(out.getTotalPrice())
                );
    }
}
