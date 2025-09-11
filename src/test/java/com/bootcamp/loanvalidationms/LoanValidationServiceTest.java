package com.bootcamp.loanvalidationms;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import com.bootcamp.loanvalidationms.domain.dto.LoanValidationResult;
import com.bootcamp.loanvalidationms.service.LoanValidationService;
import com.bootcamp.loanvalidationms.service.LoanValidator;
import com.bootcamp.loanvalidationms.service.rules.LoanHistoryRule;
import com.bootcamp.loanvalidationms.service.rules.PaymentCapacityRule;
import com.bootcamp.loanvalidationms.service.rules.TermMonthsRule;
import com.bootcamp.loanvalidationms.service.rules.ValidationDataRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanValidationServiceTest {

	@Mock
	private LoanValidator loanValidator;

	@InjectMocks
	private LoanValidationService loanValidationService;

	@Test
	void shouldReturnValidationResultFromValidator() {
		// Arrange: datos de entrada
		LoanValidationRequest request = LoanValidationRequest.builder()
				.monthlySalary(BigDecimal.valueOf(2500))
				.requestedAmount(BigDecimal.valueOf(6000))
				.termMonths(24)
				.lastLoanDate(LocalDate.of(2025, 4, 1))
				.build();

		LoanValidationResult expectedResult = LoanValidationResult.builder()
				.eligible(true)
				.monthlyPayment(BigDecimal.valueOf(250.00))
				.reasons(Collections.emptyList())
				.build();

		// Mock: comportamiento del validador
		when(loanValidator.validate(request)).thenReturn(expectedResult);

		// Act: invocar el servicio
		Mono<LoanValidationResult> resultMono = loanValidationService.validate(request);

		// Assert: verificar resultado
		StepVerifier.create(resultMono)
				.expectNextMatches(result ->
						result.isEligible() &&
								result.getMonthlyPayment().compareTo(BigDecimal.valueOf(250.00)) == 0 &&
								result.getReasons().isEmpty()
				)
				.verifyComplete();

		// Verificar que el validador fue llamado
		verify(loanValidator).validate(request);
	}
}
