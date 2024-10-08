package com.tinqinacademy.hotel.api.operations.system.registervisitors;


import com.tinqinacademy.hotel.api.base.OperationInput;
import com.tinqinacademy.hotel.api.model.input.VisitorRegisterInput;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterVisitorsInput implements OperationInput {
    @NotEmpty(message = "The list of visitors must be at least 1")
    private List<@Valid VisitorRegisterInput> visitorRegisterInputs;
}
