package com.bootcamp.loanvalidationms.service;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import com.bootcamp.loanvalidationms.domain.dto.LoanValidationResponse;
import com.bootcamp.loanvalidationms.service.rules.PaymentCapacityRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanValidationService {

    private final List<ValidationRule> rules;         // R1..R4
    private final PaymentCapacityRule paymentRule;    // para computeMonthlyPayment

    public Mono<LoanValidationResponse> validate(LoanValidationRequest request) {
        return Flux.fromIterable(rules)
                .flatMap(r -> r.validate(request))
                .flatMapIterable(x -> x)
                .collectList()
                .map(reasons -> {
                    BigDecimal monthlyPayment = null;
                    if (request.getRequestedAmount() != null
                            && request.getTermMonths() != null
                            && request.getTermMonths() > 0) {
                        monthlyPayment = paymentRule.computeMonthlyPayment(request);
                    }
                    return LoanValidationResponse.builder()
                            .eligible(reasons.isEmpty())
                            .reasons(reasons)
                            .monthlyPayment(monthlyPayment)
                            .build();
                });
    }
}
