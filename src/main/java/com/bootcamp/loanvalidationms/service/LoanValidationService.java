package com.bootcamp.loanvalidationms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import com.bootcamp.loanvalidationms.domain.dto.LoanValidationResult;
import com.bootcamp.loanvalidationms.service.rules.PaymentCapacityRule;
import com.bootcamp.loanvalidationms.service.rules.ValidationDataRule;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanValidationService {

  private final ValidationDataRule validDataRule;

  private final PaymentCapacityRule paymentCapacityRule;

  public Mono<LoanValidationResult> validate(LoanValidationRequest request) {
    List<String> reasons = new ArrayList<>();

    // R4: Validación de datos
    validDataRule.apply(request, reasons);

    // R3: Capacidad de pago
    LoanValidationResult partialResult = paymentCapacityRule.apply(request, reasons);

    boolean eligible = reasons.isEmpty();
    return Mono.just(LoanValidationResult.builder().eligible(eligible).reasons(reasons)
        .monthlyPayment(partialResult.getMonthlyPayment()).build());
  }

}
