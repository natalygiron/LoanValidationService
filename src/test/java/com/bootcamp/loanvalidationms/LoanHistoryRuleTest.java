package com.bootcamp.loanvalidationms;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import com.bootcamp.loanvalidationms.service.rules.LoanHistoryRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoanHistoryRuleTest {

    private LoanHistoryRule rule;

    @BeforeEach
    void setup() {
        rule = new LoanHistoryRule(Clock.fixed(LocalDate.of(2025, 9, 11).atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault()));
    }

    @Test
    void shouldReturnEmptyWhenNoLastLoanDate() {
        var request = LoanValidationRequest.builder().build();
        assertTrue(rule.apply(request).isEmpty());
    }

    @Test
    void shouldReturnErrorWhenLoanIsRecent() {
        var request = LoanValidationRequest.builder().lastLoanDate(LocalDate.of(2025, 8, 1)).build();
        assertEquals(Optional.of("HAS_RECENT_LOANS"), rule.apply(request));
    }

    @Test
    void shouldPassWhenLoanIsOld() {
        var request = LoanValidationRequest.builder().lastLoanDate(LocalDate.of(2025, 5, 1)).build();
        assertTrue(rule.apply(request).isEmpty());
    }
}
