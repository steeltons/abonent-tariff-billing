package org.jenjetsu.com.hrs.deserializer;

import org.jenjetsu.com.hrs.model.CallOptionHrs;
import org.jenjetsu.com.hrs.model.DurationShell;
import org.jenjetsu.com.hrs.model.implementation.CallOptionImpl;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.function.Function;

@Component
public class CallOptionDeserializer implements Function<String, CallOptionHrs> {

    /**
     * <h2>callOptionDeserialize</h2>
     * <p>Deserialize call option from string</p>
     * @param s input call option information
     * @return CallOptionImpl
     */
    @Override
    public CallOptionHrs apply(String s) {
        if(s.startsWith("-")) {
            s = s.substring(s.indexOf(" ") + 1, s.length());
        }
        String[] words = s.split(" ");
        Long minuteBuffer = Long.parseLong(words[2]);
        return CallOptionImpl.builder()
                .callOptionId(Long.parseLong(words[0]))
                .minuteCost(Float.parseFloat(words[1]))
                .minuteBuffer(minuteBuffer > 0 ? DurationShell.ofMinutes(minuteBuffer) : DurationShell.INFINITY)
                .build();
    }
}
