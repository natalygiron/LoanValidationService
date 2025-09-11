package com.bootcamp.loanvalidationms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import com.bootcamp.loanvalidationms.domain.dto.LoanValidationResult;
import com.bootcamp.loanvalidationms.service.rules.LoanHistoryRule;
import com.bootcamp.loanvalidationms.service.rules.PaymentCapacityRule;
import com.bootcamp.loanvalidationms.service.rules.TermMonthsRule;
import com.bootcamp.loanvalidationms.service.rules.ValidationDataRule;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanValidationService {

  private final ValidationDataRule validDataRule; // R4
  private final TermMonthsRule termMonthsRule; // R2
  private final LoanHistoryRule loanHistoryRule; // R1
  private final PaymentCapacityRule paymentCapacityRule; // R3

  public Mono<LoanValidationResult> validate(LoanValidationRequest request) {
    List<String> reasons = new ArrayList<>();

    // R4: datos básicos > 0
    validDataRule.apply(request, reasons);

    // R2: 1..36
    termMonthsRule.apply(request, reasons);

    // R1: últimos 3 meses (incluyente)
    loanHistoryRule.apply(request, reasons);

    // R3: capacidad de pago (+ calcula monthlyPayment si puede)
    var partial = paymentCapacityRule.apply(request, reasons);

    boolean eligible = reasons.isEmpty();
    return Mono.just(LoanValidationResult.builder().eligible(eligible).reasons(reasons)
        .monthlyPayment(partial.getMonthlyPayment()).build());
  }
}
