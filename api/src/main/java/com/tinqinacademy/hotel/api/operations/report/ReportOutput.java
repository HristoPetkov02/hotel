package com.tinqinacademy.hotel.api.operations.report;


import com.tinqinacademy.hotel.api.base.OperationOutput;
import com.tinqinacademy.hotel.api.model.output.VisitorReportOutput;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportOutput implements OperationOutput {
    private List<VisitorReportOutput> visitors;
}
