package org.jenjetsu.com.brt.deserializer;

import org.jenjetsu.com.core.entity.AbonentBill;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class AbonentBillDeserializer implements Function<String, AbonentBill> {

    /**
     * <h2>abonentBillDeserializer</h2>
     * <p>Convert input string into AbonentBill</p>
     * @param s - input string
     * @return AbonentBill
     * @exception java.lang.NumberFormatException
     */
    public AbonentBill apply(String s) {
        String[] words = s.split(" ");
        return AbonentBill.builder()
                .phoneNumber(Long.parseLong(words[0]))
                .totalCost(Double.parseDouble(words[1]))
                .build();
    }
}
