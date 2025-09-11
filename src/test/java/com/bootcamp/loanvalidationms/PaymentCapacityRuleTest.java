package com.bootcamp.loanvalidationms;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import com.bootcamp.loanvalidationms.service.rules.PaymentCapacityRule;

@ExtendWith(MockitoExtension.class)
class PaymentCapacityRuleTest {

    private PaymentCapacityRule rule;

    @BeforeEach
    void setup() {
        rule = new PaymentCapacityRule();
    }

    @Test
    void shouldReturnEmptyWhenPaymentIsWithinLimit() {
        var request = LoanValidationRequest.builder()
                .monthlySalary(BigDecimal.valueOf(2500))
                .requestedAmount(BigDecimal.valueOf(6000))
                .termMonths(24)
                .build();

        assertTrue(rule.apply(request).isEmpty());
    }

    @Test
    void shouldReturnErrorWhenPaymentExceedsLimit() {
        var request = LoanValidationRequest.builder()
                .monthlySalary(BigDecimal.valueOf(1000))
                .requestedAmount(BigDecimal.valueOf(6000))
                .termMonths(12)
                .build();

        assertEquals(Optional.of("CAPACIDAD_INSUFICIENTE"), rule.apply(request));
    }

    @Test
    void shouldReturnEmptyWhenSalaryIsNull() {
        var request = LoanValidationRequest.builder()
                .monthlySalary(null)
                .requestedAmount(BigDecimal.valueOf(6000))
                .termMonths(12)
                .build();

        assertEquals(Optional.of("CAPACIDAD_INSUFICIENTE"), rule.apply(request));
    }

    @Test
    void shouldReturnEmptyWhenTermIsInvalid() {
        var request = LoanValidationRequest.builder()
                .monthlySalary(BigDecimal.valueOf(2500))
                .requestedAmount(BigDecimal.valueOf(6000))
                .termMonths(0)
                .build();

        assertTrue(rule.apply(request).isEmpty());
    }

    @Test
    void shouldCalculateMonthlyPaymentCorrectly() {
        var request = LoanValidationRequest.builder()
                .requestedAmount(BigDecimal.valueOf(6000))
                .termMonths(24)
                .build();

        var expected = BigDecimal.valueOf(250.00).setScale(2);
        assertEquals(expected, rule.calculateMonthlyPayment(request));
    }
}
