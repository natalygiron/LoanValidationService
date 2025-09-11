package com.bootcamp.loanvalidationms;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import com.bootcamp.loanvalidationms.service.rules.TermMonthsRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TermMonthsRuleTest {

    private TermMonthsRule rule;

    @BeforeEach
    void setup() {
        rule = new TermMonthsRule();
    }

    @Test
    void shouldReturnEmptyWhenTermIsValid() {
        var request = LoanValidationRequest.builder().termMonths(24).build();
        assertTrue(rule.apply(request).isEmpty());
    }

    @Test
    void shouldReturnErrorWhenTermIsTooShort() {
        var request = LoanValidationRequest.builder().termMonths(0).build();
        assertEquals(Optional.of("PLAZO_MAXIMO_SUPERADO"), rule.apply(request));
    }

    @Test
    void shouldReturnErrorWhenTermIsTooLong() {
        var request = LoanValidationRequest.builder().termMonths(40).build();
        assertEquals(Optional.of("PLAZO_MAXIMO_SUPERADO"), rule.apply(request));
    }

    @Test
    void shouldReturnErrorWhenTermIsNull() {
        var request = LoanValidationRequest.builder().termMonths(null).build();
        assertEquals(Optional.of("PLAZO_MAXIMO_SUPERADO"), rule.apply(request));
    }
}
