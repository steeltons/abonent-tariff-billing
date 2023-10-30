package org.jenjetsu.com.brt.serializer;

import org.jenjetsu.com.brt.entity.Abonent;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import static java.lang.String.format;

@Component
public class AbonentSerializer implements Function<Abonent, String> {

    /**
     * <h2>abonentSerializer</h2>
     * <p>Serialize abonent information into string for HRS</p>
     * <p>Output example:</p>
     * <p>"<code>phone_number tariff_id</code>"</p>
     * <p>Where:</p>
     * <p><b># phone_number</b> - abonent phone number - write as long</p>
     * <p><b># tariff_id</b> - current abonent's tariff - write as UUID</p>
     * @param a - input Abonent
     * @return String
     */
    public String apply(Abonent a) {
        return format("%d %s",
                    a.getPhoneNumber(),
                    a.getTariff().getTariffId()
                );
    }
}
