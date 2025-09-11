package com.bootcamp.loanvalidationms.service.rules;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import com.bootcamp.loanvalidationms.domain.dto.LoanValidationResult;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentCapacityRule {

  private static final BigDecimal MAX_PAYMENT_RATIO = BigDecimal.valueOf(0.40);

  public LoanValidationResult apply(LoanValidationRequest request, List<String> reasons) {
    BigDecimal monthlyPayment = null;

    // Solo calcular si los datos son válidos
    if (request.getRequestedAmount() != null
            && request.getTermMonths() != null
            && request.getTermMonths() > 0) {

      monthlyPayment = request.getRequestedAmount()
              .divide(BigDecimal.valueOf(request.getTermMonths()), 2, RoundingMode.HALF_UP);

      if (request.getMonthlySalary() != null) {
        BigDecimal maxAllowed = request.getMonthlySalary().multiply(MAX_PAYMENT_RATIO);
        if (monthlyPayment.compareTo(maxAllowed) > 0) {
          reasons.add("CAPACIDAD_INSUFICIENTE");
        }
      }
    }

    boolean eligible = reasons.isEmpty();
    return LoanValidationResult.builder()
            .eligible(eligible)
            .reasons(reasons)
            .monthlyPayment(monthlyPayment)
            .build();
  }
}
