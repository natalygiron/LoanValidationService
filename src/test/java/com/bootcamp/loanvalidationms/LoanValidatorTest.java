package com.bootcamp.loanvalidationms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import com.bootcamp.loanvalidationms.domain.dto.LoanValidationResult;
import com.bootcamp.loanvalidationms.service.LoanValidator;
import com.bootcamp.loanvalidationms.service.rules.LoanHistoryRule;
import com.bootcamp.loanvalidationms.service.rules.PaymentCapacityRule;
import com.bootcamp.loanvalidationms.service.rules.TermMonthsRule;
import com.bootcamp.loanvalidationms.service.rules.ValidationDataRule;

@ExtendWith(MockitoExtension.class)
class LoanValidatorTest {

    @Mock
    private LoanHistoryRule loanHistoryRule;

    @Mock
    private TermMonthsRule termMonthsRule;

    @Mock
    private PaymentCapacityRule paymentCapacityRule;

    @Mock
    private ValidationDataRule validationDataRule;

    @InjectMocks
    private LoanValidator loanValidator;

    @Test
    void shouldReturnEligibleTrueWhenAllRulesPass() {
        // Arrange
        LoanValidationRequest request = LoanValidationRequest.builder()
                .monthlySalary(BigDecimal.valueOf(2500))
                .requestedAmount(BigDecimal.valueOf(6000))
                .termMonths(24)
                .lastLoanDate(LocalDate.of(2025, 1, 1))
                .build();

        when(validationDataRule.apply(request)).thenReturn(Collections.emptyList());
        when(loanHistoryRule.apply(request)).thenReturn(Optional.empty());
        when(termMonthsRule.apply(request)).thenReturn(Optional.empty());
        when(paymentCapacityRule.apply(request)).thenReturn(Optional.empty());
        when(paymentCapacityRule.calculateMonthlyPayment(request)).thenReturn(BigDecimal.valueOf(250.00));

        // Act
        LoanValidationResult result = loanValidator.validate(request);

        // Assert
        assertTrue(result.isEligible());
        assertEquals(BigDecimal.valueOf(250.00), result.getMonthlyPayment());
        assertTrue(result.getReasons().isEmpty());
    }

    @Test
    void shouldReturnEligibleFalseWhenSomeRulesFail() {
        // Arrange
        LoanValidationRequest request = LoanValidationRequest.builder()
                .monthlySalary(BigDecimal.valueOf(1000))
                .requestedAmount(BigDecimal.valueOf(6000))
                .termMonths(12)
                .lastLoanDate(LocalDate.of(2025, 8, 1))
                .build();

        when(validationDataRule.apply(request)).thenReturn(List.of("DATOS_INVALIDOS"));
        when(loanHistoryRule.apply(request)).thenReturn(Optional.of("HAS_RECENT_LOANS"));
        when(termMonthsRule.apply(request)).thenReturn(Optional.empty());
        when(paymentCapacityRule.apply(request)).thenReturn(Optional.of("CAPACIDAD_INSUFICIENTE"));
        when(paymentCapacityRule.calculateMonthlyPayment(request)).thenReturn(BigDecimal.valueOf(500.00));

        // Act
        LoanValidationResult result = loanValidator.validate(request);

        // Assert
        assertFalse(result.isEligible());
        assertEquals(BigDecimal.valueOf(500.00), result.getMonthlyPayment());
        assertEquals(3, result.getReasons().size());
    }
}
