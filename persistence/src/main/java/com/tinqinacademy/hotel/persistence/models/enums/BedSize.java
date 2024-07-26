package com.tinqinacademy.hotel.persistence.models.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum BedSize {
    KING_SIZED("kingSized",4),
    QUEEN_SIZED("queenSized",3),
    DOUBLE("double",2),
    SMALL_DOUBLE("smallDouble",2),
    SINGLE("single",1),

    UNKNOWN("",0);

    private final String code;
    private final Integer capacity;

    BedSize(String code, Integer capacity) {
        this.code = code;
        this.capacity = capacity;
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

    public String getName() {
        return this.name();
    }


    @JsonValue
    public String toString(){
        return this.code;
    }
}
