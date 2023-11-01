package org.jenjetsu.com.brt.deserializer;

import lombok.SneakyThrows;
import org.jenjetsu.com.brt.entity.enums.CallType;
import org.jenjetsu.com.core.entity.CallBillInformation;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.function.Function;

@Component
public class CallBillInformationDeserializer implements Function<String, CallBillInformation> {

    private final DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * <h2>callBillInformationDeserializer</h2>
     * <p>Convert input String into CallBillInformation</p>
     * @param s - input string
     * @return CallBillInformation
     * @exception java.text.ParseException, java.lang.NumberFormatException
     */
    @SneakyThrows
    public CallBillInformation apply(String s) {
        if(s.startsWith("-")) {
            s = s.substring(2);
        }
        String[] words = s.split(" ");
        return CallBillInformation.builder()
                .callType(CallType.getByCode(Byte.parseByte(words[0])).getCode())
                .callTo(Long.parseLong(words[1]))
                .startCallingDate(dateFormat.parse(words[2]))
                .endCallingDate(dateFormat.parse(words[3]))
                .duration(Long.parseLong(words[4]))
                .cost(Double.parseDouble(words[5]))
                .build();
    }
}
