package com.bootcamp.loanvalidationms.domain.dto;

import java.math.BigDecimal;
import java.util.List;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanValidationResponse {
    private boolean eligible;

    @Singular
    private List<String> reasons;

    @DecimalMin("0.0")
    private BigDecimal monthlyPayment;
}
