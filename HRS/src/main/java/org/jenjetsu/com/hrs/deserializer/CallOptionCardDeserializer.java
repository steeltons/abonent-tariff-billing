package org.jenjetsu.com.hrs.deserializer;

import org.jenjetsu.com.hrs.model.CallOptionCardHrs;
import org.jenjetsu.com.hrs.model.implementation.CallOptionCardImpl;
import org.jenjetsu.com.hrs.model.implementation.CallOptionImpl;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;
import java.util.function.Function;

@Component
public class CallOptionCardDeserializer implements Function<String, CallOptionCardHrs> {

    /**
     * <h2>callOptionCardDeserialize</h2>
     * <p>Deserialize call option card from string</p>
     * @param s input callOptionCard string
     * @return CallOptionCardImpl
     */
    public CallOptionCardHrs apply(String s) {
        if(s.startsWith("-")) {
            s = s.substring(s.indexOf("-") + 2, s.length());
        }
        String[] words = s.split(" ");
        Long sharedBufferMinutes = Long.parseLong(words[3]);
        Long inputOptionId = !words[1].equals("null") ? Long.parseLong(words[1]) : null;
        Long outputOptionId = !words[2].equals("null") ? Long.parseLong(words[2]) : null;
        return CallOptionCardImpl.builder()
                .tariffId(UUID.fromString(words[0]))
                .inputOption(CallOptionImpl.builder().callOptionId(inputOptionId).build())
                .outputOption(CallOptionImpl.builder().callOptionId(outputOptionId).build())
                .sharedBuffer(sharedBufferMinutes > 0 ? Duration.ofMinutes(sharedBufferMinutes) : null)
                .cardPriority(Byte.parseByte(words[4]))
                .cardCost(Float.parseFloat(words[5]))
                .build();
    }
}
