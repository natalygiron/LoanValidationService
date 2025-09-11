package com.bootcamp.loanvalidationms.service.rules;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import com.bootcamp.loanvalidationms.service.ValidationRule;

@Component
public class LoanHistoryRule implements ValidationRule {

  private final Clock clock;

  public LoanHistoryRule() {
    this.clock = Clock.systemUTC();
  }

  public LoanHistoryRule(Clock clock) {
    this.clock = clock;
  }

  @Override
  public Optional<String> apply(LoanValidationRequest request) {
    return Optional.ofNullable(request.getLastLoanDate())
        .filter(date -> !date.isBefore(LocalDate.now(clock).minusMonths(3)))
        .map(date -> "HAS_RECENT_LOANS");
  }
}

