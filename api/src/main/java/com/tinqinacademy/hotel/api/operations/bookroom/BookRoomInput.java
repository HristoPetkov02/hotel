package com.tinqinacademy.hotel.api.operations.bookroom;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tinqinacademy.hotel.api.base.OperationInput;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class BookRoomInput implements OperationInput {
    //това е входните данни които ще се подават за резервиране на стая
    @JsonIgnore
    private String roomId;

    @NotNull(message = "Start Date must be included")
    private LocalDate startDate;

    @NotNull(message = "End Date is required")
    private LocalDate endDate;

    @NotBlank(message = "First name must be included")
    private String firstName;

    @NotBlank(message = "Last name must be include")
    private String lastName;

    @Size(min = 5,max = 15, message = "The phone number can't be lower than 5 symbols and greater than 15")
    @NotBlank(message = "Phone number is required")
    private String phoneNo;
}
