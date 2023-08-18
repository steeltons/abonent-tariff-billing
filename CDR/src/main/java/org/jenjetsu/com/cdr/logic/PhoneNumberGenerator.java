package org.jenjetsu.com.cdr.logic;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

@Service
public class PhoneNumberGenerator {

    private final String PHONE_NUMBER_PATTENR;
    private final Faker faker;

    public PhoneNumberGenerator(@Value("${phone-number-generator.pattern}") String numberPattern) {
        PHONE_NUMBER_PATTENR = numberPattern;
        faker = new Faker();
    }

    public Long generatePhoneNumber() {
        return Long.parseLong(faker.numerify(PHONE_NUMBER_PATTENR));
    }

    public Collection<Long> generatePhoneNumbers(int count) {
        Collection<Long> numbers = new HashSet<>();
        while (numbers.size() < count) {
            numbers.add(Long.parseLong(faker.numerify(PHONE_NUMBER_PATTENR)));
        }
        return numbers;
    }
}


