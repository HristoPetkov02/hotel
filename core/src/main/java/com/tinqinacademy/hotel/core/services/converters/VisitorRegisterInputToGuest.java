package com.tinqinacademy.hotel.core.services.converters;

import com.tinqinacademy.hotel.api.model.input.VisitorRegisterInput;
import com.tinqinacademy.hotel.core.services.base.BaseConverter;
import com.tinqinacademy.hotel.persistence.models.Guest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class VisitorRegisterInputToGuest extends BaseConverter<VisitorRegisterInput, Guest> {
    @Override
    public Guest convertObject(VisitorRegisterInput input) {
        Guest output = Guest.builder()
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .phoneNumber(input.getPhoneNo())
                .idCardNumber(input.getIdCardNo())
                .idCardValidity(input.getIdCardValidity())
                .idCardIssueAuthority(input.getIdCardIssueAthority())
                .idCardIssueDate(input.getIdCardIssueDate())
                .birthDate(input.getBirthDate())
                .build();
        return output;
    }
}
