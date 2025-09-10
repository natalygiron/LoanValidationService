package com.bootcamp.loanvalidationms.service;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import com.bootcamp.loanvalidationms.domain.dto.LoanValidationResponse;
import com.bootcamp.loanvalidationms.service.rules.PaymentCapacityRule;
import com.bootcamp.loanvalidationms.service.rules.ValidationDataRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanValidationService {

    private final ValidationDataRule validDataRule;
    private final PaymentCapacityRule paymentCapacityRule;

    public Mono<LoanValidationResponse> validate(LoanValidationRequest request) {
        List<String> reasons = new ArrayList<>();

        // R4: Validación de datos
        validDataRule.apply(request, reasons);

        // R3: Capacidad de pago
        LoanValidationResponse partialResult = paymentCapacityRule.apply(request, reasons);

        boolean eligible = reasons.isEmpty();
        return Mono.just(LoanValidationResponse.builder()
                .eligible(eligible)
                .reasons(reasons)
                .monthlyPayment(partialResult.getMonthlyPayment())
                .build());
    }
}
