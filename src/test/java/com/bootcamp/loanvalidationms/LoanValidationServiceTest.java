package com.bootcamp.loanvalidationms;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import com.bootcamp.loanvalidationms.domain.dto.LoanValidationResult;
import com.bootcamp.loanvalidationms.service.LoanValidationService;
import com.bootcamp.loanvalidationms.service.rules.PaymentCapacityRule;
import com.bootcamp.loanvalidationms.service.rules.ValidationDataRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class LoanValidationServiceTest {

	private ValidationDataRule validDataRule;

	private PaymentCapacityRule paymentCapacityRule;

	private LoanValidationService service;

	@BeforeEach
	void setUp() {
		validDataRule = mock(ValidationDataRule.class);
		paymentCapacityRule = mock(PaymentCapacityRule.class);
		service = new LoanValidationService(validDataRule, paymentCapacityRule);
	}

	@Test
	void shouldReturnEligibleTrueWhenAllRulesPass() {
		LoanValidationRequest request = LoanValidationRequest.builder()
			.monthlySalary(BigDecimal.valueOf(2500))
			.requestedAmount(BigDecimal.valueOf(6000))
			.termMonths(24)
			.lastLoanDate(LocalDate.of(2025, 4, 1))
			.build();

		List<String> reasons = new ArrayList<>();
		LoanValidationResult partialResponse = LoanValidationResult.builder()
			.monthlyPayment(BigDecimal.valueOf(250))
			.eligible(true)
			.reasons(reasons)
			.build();

		doAnswer(invocation -> {
			List<String> r = invocation.getArgument(1);
			return null;
		}).when(validDataRule).apply(eq(request), anyList());

		when(paymentCapacityRule.apply(eq(request), anyList())).thenReturn(partialResponse);

		StepVerifier.create(service.validate(request))
			.expectNextMatches(response -> response.isEligible() && response.getReasons().isEmpty()
					&& response.getMonthlyPayment().compareTo(BigDecimal.valueOf(250)) == 0)
			.verifyComplete();
	}

	@Test
	void shouldReturnEligibleFalseWhenSalaryIsZero() {
		LoanValidationRequest request = LoanValidationRequest.builder()
			.monthlySalary(BigDecimal.ZERO)
			.requestedAmount(BigDecimal.valueOf(6000))
			.termMonths(24)
			.lastLoanDate(LocalDate.of(2025, 4, 1))
			.build();

		List<String> reasons = new ArrayList<>();
		reasons.add("DATOS_INVALIDOS");

		LoanValidationResult partialResponse = LoanValidationResult.builder()
			.monthlyPayment(BigDecimal.valueOf(250))
			.eligible(false)
			.reasons(reasons)
			.build();

		doAnswer(invocation -> {
			List<String> r = invocation.getArgument(1);
			r.add("DATOS_INVALIDOS");
			return null;
		}).when(validDataRule).apply(eq(request), anyList());

		when(paymentCapacityRule.apply(eq(request), anyList())).thenReturn(partialResponse);

		StepVerifier.create(service.validate(request))
			.expectNextMatches(response -> !response.isEligible() && response.getReasons().contains("DATOS_INVALIDOS"))
			.verifyComplete();
	}
}
