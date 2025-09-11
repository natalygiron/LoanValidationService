package com.bootcamp.loanvalidationms;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.bootcamp.loanvalidationms.controller.LoanValidationController;
import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import com.bootcamp.loanvalidationms.domain.dto.LoanValidationResult;
import com.bootcamp.loanvalidationms.service.LoanValidationService;

import lombok.RequiredArgsConstructor;

import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor
@ExtendWith(SpringExtension.class)
class LoanValidationControllerTest {

    private WebTestClient webTestClient;
    private LoanValidationService service;

    @BeforeEach
    void setup() {
        service = mock(LoanValidationService.class);
        LoanValidationController controller = new LoanValidationController(service);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    void shouldReturnEligibleTrue() {
        LoanValidationRequest request = LoanValidationRequest.builder()
                .monthlySalary(BigDecimal.valueOf(2500))
                .requestedAmount(BigDecimal.valueOf(6000))
                .termMonths(24)
                .lastLoanDate(LocalDate.of(2025, 4, 1))
                .build();

        LoanValidationResult result = LoanValidationResult.builder()
                .eligible(true)
                .reasons(List.of())
                .monthlyPayment(BigDecimal.valueOf(250))
                .build();

        when(service.validate(any())).thenReturn(Mono.just(result));

        webTestClient.post()
                .uri("/loan-validations")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.eligible").isEqualTo(true)
                .jsonPath("$.monthlyPayment").isEqualTo(250.0);
    }
}
