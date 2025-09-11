package com.bootcamp.loanvalidationms.service;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ValidationRule {
    Mono<List<String>> validate(LoanValidationRequest req);
}
