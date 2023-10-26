package org.jenjetsu.com.brt.converter;

import java.util.stream.Stream;

import org.jenjetsu.com.brt.entity.enums.CallType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CallTypeConverter implements AttributeConverter<CallType, Byte>{

    @Override
    public Byte convertToDatabaseColumn(CallType attribute) {
        if(attribute == null) {
            throw new NullPointerException("CallType in entity is null");
        }
        return attribute.getCode();
    }

    @Override
    public CallType convertToEntityAttribute(Byte dbData) {
        if(dbData == null) {
            throw new NullPointerException("CallType from database is null");
        }
        return Stream.of(CallType.values())
                     .filter(c -> c.getCode().equals(dbData))
                     .findFirst()
                     .orElseThrow(IllegalArgumentException::new);
    }
    
}
