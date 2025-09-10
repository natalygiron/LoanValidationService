package com.bootcamp.loanvalidationms.service.rules;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ValidationDataRule {

    public void apply(LoanValidationRequest request, List<String> reasons) {
        if (request.getMonthlySalary() == null || request.getMonthlySalary().compareTo(BigDecimal.ZERO) <= 0) {
            reasons.add("DATOS_INVALIDOS");
        }

        if (request.getRequestedAmount() == null || request.getRequestedAmount().compareTo(BigDecimal.ZERO) <= 0) {
            reasons.add("DATOS_INVALIDOS");
        }
    }
}
