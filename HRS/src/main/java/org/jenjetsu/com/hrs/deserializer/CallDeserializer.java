package org.jenjetsu.com.hrs.deserializer;

import org.jenjetsu.com.hrs.model.CallHrs;
import org.jenjetsu.com.hrs.model.implementation.CallImpl;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.function.Function;

@Component
public class CallDeserializer implements Function<String, CallHrs>  {

    private final DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * <h2>callDeserialize</h2>
     * <p>Deserialize abonent call from string</p>
     * @param s input abonent call informtaion
     * @return CallImpl
     */
    public CallHrs apply(String s) {
        if(s.startsWith("-")) {
            s = s.substring(s.indexOf(" ") + 1, s.length());
        }
        String[] words = s.split(" ");
        try {
            return CallImpl.builder()
                    .callType(Byte.parseByte(words[0]))
                    .callTo(Long.parseLong(words[1]))
                    .startCallingTime(format.parse(words[2]))
                    .endCallingTime(format.parse(words[3]))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(String.format("""
                        Error parsing line "%s"
                        """, s));
        }
    }
}
