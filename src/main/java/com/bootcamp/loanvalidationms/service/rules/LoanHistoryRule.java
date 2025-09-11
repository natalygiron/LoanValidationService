package com.bootcamp.loanvalidationms.service.rules;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import com.bootcamp.loanvalidationms.service.ValidationRule;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class LoanHistoryRule implements ValidationRule {

    private final Clock clock;

    public LoanHistoryRule(Clock clock) {
        this.clock = clock;
    }

    @Override
    public Mono<List<String>> validate(LoanValidationRequest req) {
        List<String> errors = new ArrayList<>();
        if (req.getLastLoanDate() != null) {
            LocalDate today = LocalDate.now(clock);
            LocalDate since = today.minusMonths(3); // incluyente
            if (!req.getLastLoanDate().isBefore(since)) {
                errors.add("ANTIGUEDAD_INSUFICIENTE: préstamo en los últimos 3 meses");
            }
        }
        return Mono.just(errors);
    }
}
