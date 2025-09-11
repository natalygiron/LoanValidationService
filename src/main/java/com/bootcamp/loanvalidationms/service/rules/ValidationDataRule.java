package com.bootcamp.loanvalidationms.service.rules;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;

@Component
public class ValidationDataRule {

  public List<String> apply(LoanValidationRequest request) {
    return Stream
        .of(validatePositive(request.getMonthlySalary()),
            validatePositive(request.getRequestedAmount()))
        .flatMap(Optional::stream).collect(Collectors.toList());
  }

  private Optional<String> validatePositive(BigDecimal value) {
    if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
      return Optional.of("DATOS_INVALIDOS");
    }
    return Optional.empty();
  }
}
