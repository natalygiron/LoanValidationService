package com.bootcamp.loanvalidationms.service.rules;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import com.bootcamp.loanvalidationms.service.ValidationRule;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

public class LoanHistoryRule implements ValidationRule {

    private final Clock clock;

    public LoanHistoryRule(Clock clock) {
        this.clock = clock;
    }

    @Override
    public Mono<List<String>> validate(LoanValidationRequest req) {
        if (req.getLastLoanDate() == null) {
            return Mono.just(List.of());
        }

        LocalDate today = LocalDate.now(clock);
        LocalDate since = today.minusMonths(3);

        if (!req.getLastLoanDate().isBefore(since)) {
            return Mono.just(List.of("El solicitante tiene préstamos en los últimos 3 meses"));
        }

        return Mono.just(List.of());
    }
}
