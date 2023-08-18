package org.jenjetsu.com.cdr.logic;

import com.github.javafaker.Faker;
import org.jenjetsu.com.core.entity.CallInformation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CallInfoCreator {

    private final Long MIN_CALLING_DURATION_SECONDS;
    private final Long MAX_CALLING_DURATION_SECONDS;
    private final Date MIN_CALLING_DATE;
    private final Date MAX_CALLING_DATE;
    private final Integer MIN_PHONE_CALLS;
    private final Integer MAX_PHONE_CALLS;
    private final Random random;
    private final Faker faker;

    public CallInfoCreator(@Value("${CallInfoCreator.min-call-duration-seconds}") Long min_duration,
                           @Value("${CallInfoCreator.max-call-duration-seconds}") Long max_duration,
                           @Value("${CallInfoCreator.min-call-date}") String min_calling_date,
                           @Value("${CallInfoCreator.max-call-date}") String max_calling_date,
                           @Value("${CallInfoCreator.min-phone-calls}") Integer min_phone_calls,
                           @Value("${CallInfoCreator.max-phone-calls}") Integer max_phone_calls) {
        if(min_duration > max_duration || min_duration < 0 || max_duration < 0) {
            throw new Error("Invalid calling duration");
        }
        if(min_phone_calls > max_phone_calls || min_phone_calls < 0 || max_phone_calls < 0) {
            throw new Error("Invalid amount of phone calls");
        }
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            MIN_CALLING_DATE = format.parse(min_calling_date);
            MAX_CALLING_DATE = format.parse(max_calling_date);
        } catch (ParseException e) {
            throw new Error(String.format("input format %s does not match with yyyy-mm-dd"));
        }
        if(MIN_CALLING_DATE.after(MAX_CALLING_DATE)) {
            throw new Error("Min calling date is after max");
        }
        MIN_CALLING_DURATION_SECONDS = min_duration;
        MAX_CALLING_DURATION_SECONDS = max_duration;
        MIN_PHONE_CALLS = min_phone_calls;
        MAX_PHONE_CALLS = max_phone_calls;
        random = new Random();
        faker = new Faker();
    }

    public Collection<CallInformation> generateCollectionOfCalls(Collection<Long> phoneNumberCollection) {
        List<CallInformation> calls = new ArrayList<>();
        List<Long> phoneNumberList = new ArrayList<>(phoneNumberCollection);
        for(Long callingPhone : phoneNumberCollection) {
            int randCallsNumber = random.nextInt(MIN_PHONE_CALLS, MAX_PHONE_CALLS);
            for(int index = 0; index < randCallsNumber; index++) {
               long durationMillis = random.nextLong(MIN_CALLING_DURATION_SECONDS, MAX_CALLING_DURATION_SECONDS) * 1000;
               Date startCallingDate = faker.date().between(MIN_CALLING_DATE, MAX_CALLING_DATE);
               Date endCallingDate = new Date(startCallingDate.getTime() + durationMillis);
               byte callType = generateRandomCallType();
               long callToNumber = 0l;
               if(isCallingToSomeoneInList() && phoneNumberList.size() != 1) {
                   while (callToNumber == 0 || callToNumber == callingPhone) {
                       callToNumber = phoneNumberList.get(random.nextInt(0, phoneNumberList.size()));
                   }
               } else {
                   while (callToNumber == 0 || phoneNumberList.contains(callToNumber)) {
                       callToNumber = Long.parseLong(faker.numerify("79146878###"));
                   }
               }
               calls.add(new CallInformation(callType, callingPhone, callToNumber, startCallingDate, endCallingDate));
            }
        }
        return calls;
    }

    private byte generateRandomCallType() {
        return (byte) random.nextInt(1, 3);
    }

    private boolean isCallingToSomeoneInList() {
        float chance = Math.round(random.nextFloat() * 100) / 100.0f;
        return chance > 0.5f;
    }
}
