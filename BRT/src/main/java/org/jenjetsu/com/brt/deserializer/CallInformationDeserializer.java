package org.jenjetsu.com.brt.deserializer;

import lombok.SneakyThrows;
import org.jenjetsu.com.core.entity.CallInformation;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.function.Function;

@Component
public class CallInformationDeserializer implements Function<String, CallInformation> {

    private final DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * <h2>callinformationDeserializer</h2>
     * <p>Deserialize string of CallInformation from CDR to object</p>
     * @param s
     * @return CallInformation
     */
    @SneakyThrows
    public CallInformation apply(String s) {
        String[] words = s.split(" ");
        return CallInformation.builder()
                .callTypeCode(Byte.parseByte(words[0]))
                .phoneNumber(Long.parseLong(words[1]))
                .callToNumber(Long.parseLong(words[2]))
                .startCallingTime(dateFormat.parse(words[3]))
                .endCallingTime(dateFormat.parse(words[4]))
                .build();
    }
}
