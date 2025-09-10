package com.bootcamp.loanvalidationms.controller;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import com.bootcamp.loanvalidationms.domain.dto.LoanValidationResponse;
import com.bootcamp.loanvalidationms.service.LoanValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/loan-validations")
@RequiredArgsConstructor
public class LoanValidationController {

    private final LoanValidationService loanValidationService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<LoanValidationResponse> validateLoan(@RequestBody LoanValidationRequest request) {
        return loanValidationService.validate(request);
    }
}
