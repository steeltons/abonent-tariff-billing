package org.jenjetsu.com.hrs.deserializer;

import org.jenjetsu.com.hrs.model.TariffHrs;
import org.jenjetsu.com.hrs.model.implementation.TariffImpl;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.Function;

@Component
public class TariffDeserializer implements Function<String, TariffHrs> {

    /**
     * <h2>tariffDeserialize</h2>
     * <p>Deserialize tariif information from string</p>
     * @param s input tariff information
     * @return TariffImpl
     */
    public TariffHrs apply(String s) {
        String[] words = s.split(" ");
        return TariffImpl.builder()
                .tariffId(UUID.fromString(words[0]))
                .baseCost(Float.parseFloat(words[1]))
                .build();
    }
}
