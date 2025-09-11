package com.bootcamp.loanvalidationms.service.rules;

import java.util.List;

import org.springframework.stereotype.Component;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;

@Component
public class TermMonthsRule {

  public void apply(LoanValidationRequest request, List<String> reasons) {
    Integer term = request.getTermMonths();
    if (term == null) {
      reasons.add("PLAZO_INVALIDO"); // faltante
      return;
    }
    if (term < 1 || term > 36) {
      reasons.add("PLAZO_INVALIDO"); // fuera de rango
    }
  }
}
