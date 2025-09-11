package com.bootcamp.loanvalidationms;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import com.bootcamp.loanvalidationms.service.rules.TermMonthsRule;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TermMonthsRuleTest {

    private final TermMonthsRule rule = new TermMonthsRule();

    @Test
    void passesOnValidRange() {
        var req = LoanValidationRequest.builder().termMonths(12).build();
        List<String> reasons = new ArrayList<>();
        rule.apply(req, reasons);
        assertTrue(reasons.isEmpty());
    }

    @Test
    void failsWhenNull() {
        var req = LoanValidationRequest.builder().termMonths(null).build();
        List<String> reasons = new ArrayList<>();
        rule.apply(req, reasons);
        assertTrue(reasons.contains("PLAZO_INVALIDO"));
    }

    @Test
    void failsBelowMin() {
        var req = LoanValidationRequest.builder().termMonths(0).build();
        List<String> reasons = new ArrayList<>();
        rule.apply(req, reasons);
        assertTrue(reasons.contains("PLAZO_INVALIDO"));
    }

    @Test
    void failsAboveMax() {
        var req = LoanValidationRequest.builder().termMonths(37).build();
        List<String> reasons = new ArrayList<>();
        rule.apply(req, reasons);
        assertTrue(reasons.contains("PLAZO_INVALIDO"));
    }
}
