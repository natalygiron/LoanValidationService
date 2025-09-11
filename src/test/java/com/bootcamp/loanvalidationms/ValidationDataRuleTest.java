package com.bootcamp.loanvalidationms;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import com.bootcamp.loanvalidationms.service.rules.ValidationDataRule;

class ValidationDataRuleTest {

    private final ValidationDataRule rule = new ValidationDataRule();

    @Test
    void shouldAddInvalidReasonForNullRequestedAmount() {
        LoanValidationRequest request = LoanValidationRequest.builder()
                .monthlySalary(BigDecimal.valueOf(2500))
                .requestedAmount(null)
                .termMonths(12)
                .build();

        List<String> reasons = new ArrayList<>();
        rule.apply(request, reasons);

        assertTrue(reasons.contains("DATOS_INVALIDOS"));
    }

    @Test
    void shouldAddInvalidReasonForZeroRequestedAmount() {
        LoanValidationRequest request = LoanValidationRequest.builder()
                .monthlySalary(BigDecimal.valueOf(2500))
                .requestedAmount(BigDecimal.ZERO)
                .termMonths(12)
                .build();

        List<String> reasons = new ArrayList<>();
        rule.apply(request, reasons);

        assertTrue(reasons.contains("DATOS_INVALIDOS"));
    }

    @Test
    void shouldAddInvalidReasonWhenRequestedAmountIsNegative() {
        LoanValidationRequest request = LoanValidationRequest.builder()
                .monthlySalary(BigDecimal.valueOf(2500))
                .requestedAmount(BigDecimal.valueOf(-2500))
                .termMonths(12)
                .build();

        List<String> reasons = new ArrayList<>();
        rule.apply(request, reasons);

        assertTrue(reasons.contains("DATOS_INVALIDOS"));
    }

    @Test
    void shouldNotAddReasonForValidRequestedAmount() {
        LoanValidationRequest request = LoanValidationRequest.builder()
                .monthlySalary(BigDecimal.valueOf(2500))
                .requestedAmount(BigDecimal.valueOf(5000))
                .termMonths(12)
                .build();

        List<String> reasons = new ArrayList<>();
        rule.apply(request, reasons);

        assertTrue(reasons.isEmpty());
    }

    @Test
    void shouldAddReasonWhenSalaryIsNull() {
        LoanValidationRequest request = LoanValidationRequest.builder()
                .monthlySalary(null)
                .requestedAmount(BigDecimal.valueOf(5000))
                .termMonths(12)
                .build();

        List<String> reasons = new ArrayList<>();
        rule.apply(request, reasons);

        assertTrue(reasons.contains("DATOS_INVALIDOS"));
    }

    @Test
    void shouldAddReasonWhenSalaryIsZero() {
        LoanValidationRequest request = LoanValidationRequest.builder()
                .monthlySalary(BigDecimal.ZERO)
                .requestedAmount(BigDecimal.valueOf(5000))
                .termMonths(12)
                .build();

        List<String> reasons = new ArrayList<>();
        rule.apply(request, reasons);

        assertTrue(reasons.contains("DATOS_INVALIDOS"));
    }

    @Test
    void shouldAddReasonWhenSalaryIsNegative() {
        LoanValidationRequest request = LoanValidationRequest.builder()
                .monthlySalary(BigDecimal.valueOf(-100))
                .requestedAmount(BigDecimal.valueOf(5000))
                .termMonths(12)
                .build();

        List<String> reasons = new ArrayList<>();
        rule.apply(request, reasons);

        assertTrue(reasons.contains("DATOS_INVALIDOS"));
    }

    @Test
    void shouldNotAddReasonWhenSalaryIsPositive() {
        LoanValidationRequest request = LoanValidationRequest.builder()
                .monthlySalary(BigDecimal.valueOf(2500))
                .requestedAmount(BigDecimal.valueOf(5000))
                .termMonths(12)
                .build();

        List<String> reasons = new ArrayList<>();
        rule.apply(request, reasons);

        assertTrue(reasons.isEmpty());
    }
}
