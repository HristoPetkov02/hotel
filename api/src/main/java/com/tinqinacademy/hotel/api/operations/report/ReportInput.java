package com.tinqinacademy.hotel.api.operations.report;

import lombok.*;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportInput {
    private LocalDate startDate;
    private LocalDate endDate;
    private Optional<String> firstName;
    private Optional<String> lastName;
    private Optional<String> phoneNo;
    private Optional<String> idCardNo;
    private Optional<LocalDate> idCardValidity;
    private Optional<String> idCardIssueAthority;
    private Optional<LocalDate> idCardIssueDate;
    private Optional<String> roomNo;
}
