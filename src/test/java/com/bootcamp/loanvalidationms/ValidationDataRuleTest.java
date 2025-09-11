package com.bootcamp.loanvalidationms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import com.bootcamp.loanvalidationms.service.rules.ValidationDataRule;

@ExtendWith(MockitoExtension.class)
class ValidationDataRuleTest {

    private ValidationDataRule rule;

    @BeforeEach
    void setup() {
        rule = new ValidationDataRule();
    }

    @Test
    void shouldReturnEmptyListWhenDataIsValid() {
        var request = LoanValidationRequest.builder()
                .monthlySalary(BigDecimal.valueOf(2500))
                .requestedAmount(BigDecimal.valueOf(6000))
                .build();

        assertTrue(rule.apply(request).isEmpty());
    }

    @Test
    void shouldReturnErrorWhenSalaryIsZero() {
        var request = LoanValidationRequest.builder()
                .monthlySalary(BigDecimal.ZERO)
                .requestedAmount(BigDecimal.valueOf(6000))
                .build();

        var reasons = rule.apply(request);
        assertTrue(reasons.contains("DATOS_INVALIDOS"));
    }

    @Test
    void shouldReturnErrorWhenRequestedAmountIsNegative() {
        var request = LoanValidationRequest.builder()
                .monthlySalary(BigDecimal.valueOf(2500))
                .requestedAmount(BigDecimal.valueOf(-100))
                .build();

        var reasons = rule.apply(request);
        assertTrue(reasons.contains("DATOS_INVALIDOS"));
    }

    @Test
    void shouldReturnBothErrorsWhenBothFieldsAreInvalid() {
        var request = LoanValidationRequest.builder()
                .monthlySalary(BigDecimal.ZERO)
                .requestedAmount(BigDecimal.valueOf(-100))
                .build();

        var reasons = rule.apply(request);
        assertEquals(2, reasons.size());
        assertTrue(reasons.contains("DATOS_INVALIDOS"));
    }
}
