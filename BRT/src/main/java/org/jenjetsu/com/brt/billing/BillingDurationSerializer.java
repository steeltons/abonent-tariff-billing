package org.jenjetsu.com.brt.billing;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.io.IOException;
import java.time.Duration;

public class BillingDurationSerializer extends JsonSerializer<Duration> {


    @Override
    public void serialize(Duration value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if(value != null) {
            gen.writeString(DurationFormatUtils.formatDuration(value.toMillis(), "HH:mm:ss"));
        } else {
            throw new IllegalArgumentException("Duration cannot be null");
        }
    }
}
