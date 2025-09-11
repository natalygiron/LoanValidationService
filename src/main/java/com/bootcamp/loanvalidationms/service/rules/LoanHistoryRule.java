package com.bootcamp.loanvalidationms.service.rules;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;

@Component
public class LoanHistoryRule {

  // Para tests puedes sobrecargar ctor e inyectar un Clock fijo.
  private final Clock clock = Clock.systemUTC();

  public void apply(LoanValidationRequest request, List<String> reasons) {
    if (request.getLastLoanDate() == null)
      return;

    LocalDate today = LocalDate.now(clock);
    LocalDate since = today.minusMonths(3); // incluyente
    // falla si lastLoanDate >= since
    if (!request.getLastLoanDate().isBefore(since)) {
      reasons.add("ANTIGUEDAD_INSUFICIENTE");
    }
  }
}

