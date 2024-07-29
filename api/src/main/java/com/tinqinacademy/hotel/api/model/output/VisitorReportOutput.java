package com.tinqinacademy.hotel.api.model.output;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisitorReportOutput {
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
