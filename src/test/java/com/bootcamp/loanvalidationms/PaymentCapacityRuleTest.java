package com.bootcamp.loanvalidationms;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import com.bootcamp.loanvalidationms.domain.dto.LoanValidationResult;
import com.bootcamp.loanvalidationms.service.rules.PaymentCapacityRule;

class PaymentCapacityRuleTest {

    private final PaymentCapacityRule rule = new PaymentCapacityRule();

    @Test
    void shouldAddReasonWhenPaymentExceedsCapacity() {
        LoanValidationRequest request = LoanValidationRequest.builder()
                .monthlySalary(BigDecimal.valueOf(1000))
                .requestedAmount(BigDecimal.valueOf(6000))
                .termMonths(12)
                .build();

        List<String> reasons = new ArrayList<>();
        LoanValidationResult result = rule.apply(request, reasons);

        assertFalse(result.isEligible());
        assertTrue(reasons.contains("CAPACIDAD_INSUFICIENTE"));
    }

    @Test
    void shouldNotAddReasonWhenPaymentIsWithinCapacity() {
        LoanValidationRequest request = LoanValidationRequest.builder()
                .monthlySalary(BigDecimal.valueOf(3000))
                .requestedAmount(BigDecimal.valueOf(6000))
                .termMonths(24)
                .build();

        List<String> reasons = new ArrayList<>();
        LoanValidationResult result = rule.apply(request, reasons);

        assertTrue(result.isEligible());
        assertTrue(reasons.isEmpty());
    }
}
