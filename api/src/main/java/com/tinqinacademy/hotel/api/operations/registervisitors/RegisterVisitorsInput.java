package com.tinqinacademy.hotel.api.operations.registervisitors;


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
public class RegisterVisitorsInput {
    @NotEmpty(message = "The list of visitors must be at least 1")
    @Valid
    private List<VisitorRegisterInput> visitorRegisterInputs;
}
