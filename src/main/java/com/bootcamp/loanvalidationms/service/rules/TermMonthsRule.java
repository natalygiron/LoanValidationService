package com.bootcamp.loanvalidationms.service.rules;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import com.bootcamp.loanvalidationms.service.ValidationRule;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class TermMonthsRule implements ValidationRule {

    @Override
    public Mono<List<String>> validate(LoanValidationRequest req) {
        List<String> errors = new ArrayList<>();
        Integer term = req.getTermMonths();

        if (term == null) {
            errors.add("El plazo en meses es obligatorio");
        } else if (term < 1 || term > 36) {
            errors.add("El plazo debe estar entre 1 y 36 meses");
        }

        return Mono.just(errors);
    }
}
