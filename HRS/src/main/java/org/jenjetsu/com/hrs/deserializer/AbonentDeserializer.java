package org.jenjetsu.com.hrs.deserializer;

import org.jenjetsu.com.hrs.model.AbonentHrs;
import org.jenjetsu.com.hrs.model.implementation.AbonentImpl;
import org.jenjetsu.com.hrs.model.implementation.TariffImpl;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.Function;

@Component
public class AbonentDeserializer implements Function<String, AbonentHrs> {

    /**
     * <h2>abonentDeserialize</h2>
     * <p>Deserialize abonent from input string</p>
     * @param s abonent information
     * @return AbonentImpl
     */
    public AbonentHrs apply(String s) {
        String[] words = s.split(" ");
        return AbonentImpl.builder()
                .phoneNumber(Long.parseLong(words[0]))
                .tariff(TariffImpl.builder().tariffId(UUID.fromString(words[1])).build())
                .build();
    }
}
