package com.bootcamp.loanvalidationms.service;

import java.util.Optional;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;

@FunctionalInterface
public interface ValidationRule {
  Optional<String> apply(LoanValidationRequest request);
}
