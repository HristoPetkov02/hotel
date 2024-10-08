package com.tinqinacademy.hotel.api.operations.system.report;

import com.tinqinacademy.hotel.api.base.OperationInput;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportInput implements OperationInput {
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    @NotNull(message = "End date is required")
    private LocalDate endDate;
    private String firstName;
    private String lastName;
    private String phoneNo;
    private String idCardNo;
    private LocalDate idCardValidity;
    private String idCardIssueAthority;
    private LocalDate idCardIssueDate;
    private String roomNo;
}
