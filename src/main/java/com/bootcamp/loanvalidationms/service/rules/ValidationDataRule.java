package com.bootcamp.loanvalidationms.service.rules;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import com.bootcamp.loanvalidationms.service.ValidationRule;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class ValidationDataRule implements ValidationRule {

    @Override
    public Mono<List<String>> validate(LoanValidationRequest request) {
        List<String> reasons = new ArrayList<>();

        if (request.getMonthlySalary() == null || request.getMonthlySalary().compareTo(BigDecimal.ZERO) <= 0) {
            reasons.add("DATOS_INVALIDOS: monthlySalary debe ser > 0");
        }
        if (request.getRequestedAmount() == null || request.getRequestedAmount().compareTo(BigDecimal.ZERO) <= 0) {
            reasons.add("DATOS_INVALIDOS: requestedAmount debe ser > 0");
        }
        if (request.getTermMonths() == null) {
            reasons.add("DATOS_INVALIDOS: termMonths es obligatorio");
        }

        return Mono.just(reasons);
    }
}
