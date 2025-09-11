package com.bootcamp.loanvalidationms.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import com.bootcamp.loanvalidationms.domain.dto.LoanValidationResult;
import com.bootcamp.loanvalidationms.service.rules.LoanHistoryRule;
import com.bootcamp.loanvalidationms.service.rules.PaymentCapacityRule;
import com.bootcamp.loanvalidationms.service.rules.TermMonthsRule;
import com.bootcamp.loanvalidationms.service.rules.ValidationDataRule;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoanValidator {

  private final LoanHistoryRule loanHistoryRule;
  private final TermMonthsRule termMonthsRule;
  private final PaymentCapacityRule paymentCapacityRule;
  private final ValidationDataRule validationDataRule;

  public LoanValidationResult validate(LoanValidationRequest request) {
    List<String> reasons = new ArrayList<>();

    // Validaciones de datos básicos
    reasons.addAll(validationDataRule.apply(request));

    // Aplicar reglas funcionales
    Stream.of(loanHistoryRule.apply(request), termMonthsRule.apply(request),
        paymentCapacityRule.apply(request)).flatMap(Optional::stream).forEach(reasons::add);

    BigDecimal monthlyPayment = paymentCapacityRule.calculateMonthlyPayment(request);

    return LoanValidationResult.builder().eligible(reasons.isEmpty()).reasons(reasons)
        .monthlyPayment(monthlyPayment).build();
  }
}
