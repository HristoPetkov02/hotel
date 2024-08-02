package com.tinqinacademy.hotel.api.operations.report;

import com.tinqinacademy.hotel.api.base.OperationInput;
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
    private LocalDate startDate;
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
