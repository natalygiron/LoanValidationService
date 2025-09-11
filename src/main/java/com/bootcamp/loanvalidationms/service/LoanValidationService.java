package com.bootcamp.loanvalidationms.service;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import com.bootcamp.loanvalidationms.domain.dto.LoanValidationResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class LoanValidationService {

    private final List<ValidationRule> rules;

    public Mono<LoanValidationResponse> validate(LoanValidationRequest req) {
        return Flux.fromIterable(rules)
                .flatMap(rule -> rule.validate(req))
                .flatMapIterable(list -> list)
                .collectList()
                .map(reasons -> LoanValidationResponse.builder()
                        .eligible(reasons.isEmpty())
                        .reasons(reasons)
                        .monthlyPayment(
                                req.getRequestedAmount()
                                        .divide(
                                                new java.math.BigDecimal(req.getTermMonths()),
                                                java.math.RoundingMode.HALF_UP
                                        )
                        )
                        .build()
                );
    }
}
