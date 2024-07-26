package com.tinqinacademy.hotel.api.model.input;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisitorRegisterInput {
    @NotNull(message = "Start day is required")
    private LocalDate startDate;

    @NotNull(message = "End date must be included")
    private LocalDate endDate;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Size(min = 5,max = 15, message = "The phone number can't be lower than 5 symbols and greater than 15")
    private String phoneNo;

    @NotNull(message = "Birth date must be included")
    private LocalDate birthDate;


    private String idCardNo;
    private LocalDate idCardValidity;
    private String idCardIssueAthority;
    private LocalDate idCardIssueDate;

    @NotBlank(message = "Room number is required")
    private String roomNo;
}
