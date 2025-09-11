package com.bootcamp.loanvalidationms;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import com.bootcamp.loanvalidationms.domain.dto.LoanValidationResult;
import com.bootcamp.loanvalidationms.service.LoanValidationService;
import com.bootcamp.loanvalidationms.service.rules.LoanHistoryRule;
import com.bootcamp.loanvalidationms.service.rules.PaymentCapacityRule;
import com.bootcamp.loanvalidationms.service.rules.TermMonthsRule;
import com.bootcamp.loanvalidationms.service.rules.ValidationDataRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class LoanValidationServiceTest {

	private ValidationDataRule validDataRule;   // R4
	private TermMonthsRule termMonthsRule;      // R2
	private LoanHistoryRule loanHistoryRule;    // R1
	private PaymentCapacityRule paymentCapacityRule; // R3

	private LoanValidationService service;

	@BeforeEach
	void setUp() {
		validDataRule = mock(ValidationDataRule.class);
		termMonthsRule = mock(TermMonthsRule.class);
		loanHistoryRule = mock(LoanHistoryRule.class);
		paymentCapacityRule = mock(PaymentCapacityRule.class);

		service = new LoanValidationService(
				validDataRule, termMonthsRule, loanHistoryRule, paymentCapacityRule
		);
	}

	@Test
	void shouldReturnEligibleTrueWhenAllRulesPass() {
		LoanValidationRequest request = LoanValidationRequest.builder()
				.monthlySalary(BigDecimal.valueOf(2500))
				.requestedAmount(BigDecimal.valueOf(6000))
				.termMonths(24)
				.lastLoanDate(LocalDate.of(2025, 4, 1))
				.build();

		// Ninguna regla agrega errores
		doAnswer(invocation -> null).when(validDataRule).apply(eq(request), anyList());
		doAnswer(invocation -> null).when(termMonthsRule).apply(eq(request), anyList());
		doAnswer(invocation -> null).when(loanHistoryRule).apply(eq(request), anyList());

		// R3 devuelve monthlyPayment=250 y no agrega errores
		LoanValidationResult partial = LoanValidationResult.builder()
				.eligible(true)
				.reasons(new ArrayList<>())
				.monthlyPayment(BigDecimal.valueOf(250))
				.build();
		when(paymentCapacityRule.apply(eq(request), anyList())).thenReturn(partial);

		StepVerifier.create(service.validate(request))
				.expectNextMatches(res ->
						res.isEligible()
								&& res.getReasons().isEmpty()
								&& res.getMonthlyPayment().compareTo(BigDecimal.valueOf(250)) == 0
				)
				.verifyComplete();
	}

	@Test
	void shouldReturnEligibleFalseWhenSalaryIsZero() {
		LoanValidationRequest request = LoanValidationRequest.builder()
				.monthlySalary(BigDecimal.ZERO) // R4 debería marcar error
				.requestedAmount(BigDecimal.valueOf(6000))
				.termMonths(24)
				.lastLoanDate(LocalDate.of(2025, 4, 1))
				.build();

		// Simula que R4 agrega "DATOS_INVALIDOS"
		doAnswer(invocation -> {
			List<String> reasons = invocation.getArgument(1);
			reasons.add("DATOS_INVALIDOS");
			return null;
		}).when(validDataRule).apply(eq(request), anyList());

		// R2 y R1 no agregan errores
		doAnswer(invocation -> null).when(termMonthsRule).apply(eq(request), anyList());
		doAnswer(invocation -> null).when(loanHistoryRule).apply(eq(request), anyList());

		// R3 devuelve monthlyPayment (no importa elegibilidad aquí)
		LoanValidationResult partial = LoanValidationResult.builder()
				.eligible(false)
				.reasons(new ArrayList<>())
				.monthlyPayment(BigDecimal.valueOf(250))
				.build();
		when(paymentCapacityRule.apply(eq(request), anyList())).thenReturn(partial);

		StepVerifier.create(service.validate(request))
				.expectNextMatches(res ->
						!res.isEligible()
								&& res.getReasons().contains("DATOS_INVALIDOS")
								&& res.getMonthlyPayment().compareTo(BigDecimal.valueOf(250)) == 0
				)
				.verifyComplete();
	}
}
