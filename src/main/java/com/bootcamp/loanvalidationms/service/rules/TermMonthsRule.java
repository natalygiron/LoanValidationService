package com.bootcamp.loanvalidationms.service.rules;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import com.bootcamp.loanvalidationms.service.ValidationRule;

@Component
public class TermMonthsRule implements ValidationRule {

  @Override
  public Optional<String> apply(LoanValidationRequest request) {
    Integer term = request.getTermMonths();
    if (term == null || term < 1 || term > 36) {
      return Optional.of("PLAZO_MAXIMO_SUPERADO");
    }
    return Optional.empty();
  }
}
