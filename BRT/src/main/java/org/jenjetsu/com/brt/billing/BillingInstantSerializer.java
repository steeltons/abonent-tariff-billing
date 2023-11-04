package org.jenjetsu.com.brt.billing;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class BillingInstantSerializer extends JsonSerializer<Instant> {

    private final DateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    @Override
    public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if(value != null) {
            gen.writeString(format.format(Date.from(value)));
        } else {
            gen.writeString("wasn't started");
        }
    }
}
