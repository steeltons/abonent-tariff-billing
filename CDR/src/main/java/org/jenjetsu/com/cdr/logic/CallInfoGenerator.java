package org.jenjetsu.com.cdr.logic;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jenjetsu.com.cdr.database.PhoneNumberService;
import org.jenjetsu.com.core.dto.PhoneNumberListDto;
import org.jenjetsu.com.core.entity.CallInformation;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
@AllArgsConstructor
@Slf4j
public class CallInfoGenerator implements Supplier<Collection<CallInformation>> {

    private final CallInfoCreator callsInfoCreator;
    private final PhoneNumberService phoneNumberService;

    /**
     * <h2>generateCallInformation</h2>
     * <p>Generate call information from phone numbers that exists in database</p>
     * @return cdr file as resource
     */
    @Override
    public Collection<CallInformation> get() {
        Collection<Long> phoneNumbers = phoneNumberService.getNotBlockedPhoneNumbers();
        return callsInfoCreator.generateCollectionOfCalls(phoneNumbers);
    }
}
