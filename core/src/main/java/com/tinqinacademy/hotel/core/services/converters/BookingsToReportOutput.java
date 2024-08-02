package com.tinqinacademy.hotel.core.services.converters;

import com.tinqinacademy.hotel.api.model.output.VisitorReportOutput;
import com.tinqinacademy.hotel.api.operations.report.ReportOutput;
import com.tinqinacademy.hotel.core.services.base.BaseConverter;
import com.tinqinacademy.hotel.persistence.models.Booking;
import com.tinqinacademy.hotel.persistence.models.Guest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class BookingsToReportOutput extends BaseConverter<List<Booking>, ReportOutput> {
    @Override
    public ReportOutput convertObject(List<Booking> input) {
        List<VisitorReportOutput> visitors = new ArrayList<>();

        for ( Booking booking : input ) {
            if (!booking.getGuests().isEmpty()) {
                for (Guest guest : booking.getGuests()) {
                    VisitorReportOutput visitor = VisitorReportOutput.builder()
                            .startDate(booking.getStartDate())
                            .endDate(booking.getEndDate())
                            .firstName(guest.getFirstName())
                            .lastName(guest.getLastName())
                            .phoneNo(guest.getPhoneNumber())
                            .idCardNo(guest.getIdCardNumber())
                            .idCardValidity(guest.getIdCardValidity())
                            .idCardIssueAthority(guest.getIdCardIssueAuthority())
                            .idCardIssueDate(guest.getIdCardIssueDate())
                            .roomNo(booking.getRoom().getRoomNumber())
                            .build();
                    visitors.add(visitor);
                }
            }
            else {
                VisitorReportOutput visitor = VisitorReportOutput.builder()
                        .startDate(booking.getStartDate())
                        .endDate(booking.getEndDate())
                        .roomNo(booking.getRoom().getRoomNumber())
                        .build();
                visitors.add(visitor);
            }
        }

        ReportOutput output = ReportOutput.builder()
                .visitors(visitors)
                .build();
        return output;
    }
}
