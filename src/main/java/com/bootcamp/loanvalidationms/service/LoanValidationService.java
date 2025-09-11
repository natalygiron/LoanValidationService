package com.bootcamp.loanvalidationms.service;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import com.bootcamp.loanvalidationms.domain.dto.LoanValidationResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanValidationService {

  private final LoanValidator loanValidator;

  public Mono<LoanValidationResult> validate(LoanValidationRequest request) {
    return Mono.fromSupplier(() -> loanValidator.validate(request));
  }
}
