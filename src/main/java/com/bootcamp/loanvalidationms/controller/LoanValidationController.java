package com.bootcamp.loanvalidationms.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import com.bootcamp.loanvalidationms.domain.dto.LoanValidationResult;
import com.bootcamp.loanvalidationms.service.LoanValidationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/loan-validations")
@RequiredArgsConstructor
public class LoanValidationController {

  private final LoanValidationService loanValidationService;

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<LoanValidationResult> validateLoan(@RequestBody LoanValidationRequest request) {
    return loanValidationService.validate(request);
  }
}
