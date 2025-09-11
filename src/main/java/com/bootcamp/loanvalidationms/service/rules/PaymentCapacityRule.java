package com.bootcamp.loanvalidationms.service.rules;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import com.bootcamp.loanvalidationms.service.ValidationRule;

@Component
public class PaymentCapacityRule implements ValidationRule {

  private static final BigDecimal MAX_PAYMENT_RATIO = BigDecimal.valueOf(0.40);

  @Override
  public Optional<String> apply(LoanValidationRequest request) {
    if (request.getRequestedAmount() == null || request.getTermMonths() == null
        || request.getTermMonths() <= 0) {
      return Optional.empty();
    }

    BigDecimal monthlyPayment = calculateMonthlyPayment(request);

    if (request.getMonthlySalary() == null
        || monthlyPayment.compareTo(request.getMonthlySalary().multiply(MAX_PAYMENT_RATIO)) > 0) {
      return Optional.of("CAPACIDAD_INSUFICIENTE");
    }

    return Optional.empty();
  }

  public BigDecimal calculateMonthlyPayment(LoanValidationRequest request) {
    return request.getRequestedAmount().divide(BigDecimal.valueOf(request.getTermMonths()), 2,
        RoundingMode.HALF_UP);
  }
}
