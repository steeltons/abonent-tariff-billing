package org.jenjetsu.com.cdr.logic;

import static java.lang.String.format;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;
import java.util.function.Function;

import org.jenjetsu.com.core.entity.ByteArrayResourceWrapper;
import org.jenjetsu.com.core.entity.CallInformation;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CdrFileResourceGenerator implements Function<Collection<CallInformation>, Resource>{

    private final Function<CallInformation, String> callSerializer;

    /**
     * <h2>createCdrResource</h2>
     * <p>Create cdr file from list of CallInformation</p>
     * <p>Output example:</p>
     * <p>
     * call_type phone_number_1 call_to_N start_calling_time end_calling_time<br>
     * call_type phone_number_2 call_to_1 start_calling_time end_calling_time<br>
     * call_type phone_number_1 call_to_2 start_calling_time end_calling_time<br>
     * ..................................................................<br>
     * call_type phone_number_N call_to_1 start_calling_time end_calling_time
     * </p>
     * @param callInformationList list of CallInformation
     * @return ByteArrayResourceWrapper - cdr file
     */    
    public Resource apply(Collection<CallInformation> callInformationList) {
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Iterator<CallInformation> callIterator = callInformationList.iterator();
            while (callIterator.hasNext()) {
                CallInformation call = callIterator.next();
                outputStream.write(this.callSerializer.apply(call).getBytes());
                if(callIterator.hasNext()) {
                    outputStream.write((byte) '\n');
                }
            }
            return ByteArrayResourceWrapper.cdrResource(outputStream.toByteArray());
        } catch (Exception e) {
            String message = format("Impossible to create cdr file. Error message: %s", e.getMessage());
            throw new RuntimeException(message);
        }
    }

}
