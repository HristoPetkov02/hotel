package com.tinqinacademy.hotel.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BedSize {
    KING_SIZED("kingSized"),
    QUEEN_SIZED("queenSized"),
    DOUBLE("double"),
    SMALL_DOUBLE("smallDouble"),
    SINGLE("single"),

    UNKNOWN("");

    private final String code;

    BedSize(String code) {
        this.code = code;
    }

    @JsonCreator
    public static BedSize getByCode(String code){
        for (BedSize bedSize : BedSize.values()) {
            if (bedSize.code.equals(code)) {
                return bedSize;
            }
        }
        return UNKNOWN;
    }

    @JsonValue
    public String toString(){
        return this.code;
    }
}
