package com.tinqinacademy.hotel.api.operations.getroom;



import com.tinqinacademy.hotel.api.base.OperationOutput;
import com.tinqinacademy.hotel.api.model.BathroomType;
import com.tinqinacademy.hotel.api.model.BedSize;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetRoomOutput implements OperationOutput {
    //Това е изхода от резултата за намиране на стая с подадено id
    private String id;
    private BigDecimal price;
    private Integer floor;
    //private BedSize bedSize;
    private List<BedSize> bedSizes;
    private BathroomType bathroomType;
    private Integer bedCount;
    private List<LocalDate> datesOccupied;
}
