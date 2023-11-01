package org.jenjetsu.com.cdr.database;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class PhoneNumberService {

    private final JdbcTemplate jdbcTemplate;

    public Collection<Long> getNotBlockedPhoneNumbers() {
        return jdbcTemplate.query("SELECT phone_number FROM abonent WHERE NOT IS_BLOCKED",
                (rs, i) -> rs.getLong("phone_number"));
    }
}
