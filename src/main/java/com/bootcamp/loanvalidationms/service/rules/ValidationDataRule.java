package com.bootcamp.loanvalidationms.service.rules;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;

@Component
public class ValidationDataRule {

  public void apply(LoanValidationRequest request, List<String> reasons) {
    validateSalary(request.getMonthlySalary(), reasons);
    validateRequestedAmount(request.getRequestedAmount(), reasons);
  }

  private void validateSalary(BigDecimal salary, List<String> reasons) {
    if (salary == null || salary.compareTo(BigDecimal.ZERO) <= 0) {
      reasons.add("DATOS_INVALIDOS");
    }
  }

  private void validateRequestedAmount(BigDecimal amount, List<String> reasons) {
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      reasons.add("DATOS_INVALIDOS");
    }
  }
}
