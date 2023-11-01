package org.jenjetsu.com.brt.serializer;

import org.jenjetsu.com.brt.entity.Tariff;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.function.Function;
import static java.lang.String.format;

@Component
public class TariffSerializer implements Function<Tariff, String> {

    private final DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.US);
    private final DecimalFormat baseCostFormat = new DecimalFormat("###.##");

    /**
     * <h2>tariffSerializer</h2>
     * <p>Convert Tariff to formatted string</p>
     * <p>Output Example:</p>
     * <code>"tariff_id base_cost"</code>
     * <p>Where:</p>
     * <p><b># tariff_id</b> - an id of tariff - write as string</p>
     * <p><b># base_cost</b> - tariff base cost - write as float</p>
     * @param t - input tariff
     * @return String
     */
    public String apply(Tariff t) {
        return format("%s %s",
                    t.getTariffId().toString(),
                    baseCostFormat.format(t.getBaseCost().floatValue())
                );
    }
}
