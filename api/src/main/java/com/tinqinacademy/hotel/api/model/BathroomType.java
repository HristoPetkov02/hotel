package com.tinqinacademy.hotel.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BathroomType {
    PRIVATE("private"),
    SHARED("shared"),
    UNKNOWN("");

    private final String code;

    BathroomType(String code) {
        this.code = code;
    }

    @JsonValue
    public String toString() {
        return this.code;
    }

    @JsonCreator
    public static BathroomType getByCode(String code){
        for (BathroomType bathroomType : BathroomType.values()) {
            if (bathroomType.code.equals(code)) {
                return bathroomType;
            }
        }
        return UNKNOWN;
    }
}
