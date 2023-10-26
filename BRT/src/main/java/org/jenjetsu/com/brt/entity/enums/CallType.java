package org.jenjetsu.com.brt.entity.enums;

import java.util.stream.Stream;

public enum CallType {
    INPUT((byte) 1), OUTPUT((byte) 0);
    private final Byte code;
    CallType(Byte code) {
        this.code = code;
    }
    public Byte getCode() {
        return this.code;
    }

    public static CallType getByCode(Byte code) {
        return Stream.of(CallType.values())
                     .filter(c -> c.code.equals(code))
                     .findFirst()
                     .orElseThrow(IllegalArgumentException::new);
    }
}
