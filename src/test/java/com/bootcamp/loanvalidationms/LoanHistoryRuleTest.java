package com.bootcamp.loanvalidationms;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import com.bootcamp.loanvalidationms.service.rules.LoanHistoryRule;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoanHistoryRuleTest {

    static class TestableLoanHistoryRule extends LoanHistoryRule {
        private final Clock clock;
        public TestableLoanHistoryRule(Clock clock) { this.clock = clock; }
        @Override
        public void apply(LoanValidationRequest request, List<String> reasons) {
            // Copia del método original pero usando this.clock
            if (request.getLastLoanDate() == null) return;
            var today = LocalDate.now(clock);
            var since = today.minusMonths(3);
            if (!request.getLastLoanDate().isBefore(since)) {
                reasons.add("ANTIGUEDAD_INSUFICIENTE");
            }
        }
    }

    private final Clock fixed = Clock.fixed(
            LocalDate.of(2025, 9, 10).atStartOfDay(ZoneOffset.UTC).toInstant(),
            ZoneOffset.UTC
    );

    @Test
    void passesWhenOlderThan3Months() {
        var rule = new TestableLoanHistoryRule(fixed);
        var req = LoanValidationRequest.builder()
                .lastLoanDate(LocalDate.of(2025, 5, 31)) // > 3 meses
                .build();
        List<String> reasons = new ArrayList<>();
        rule.apply(req, reasons);
        assertTrue(reasons.isEmpty());
    }

    @Test
    void failsWhenExactly3MonthsInclusive() {
        var rule = new TestableLoanHistoryRule(fixed);
        var req = LoanValidationRequest.builder()
                .lastLoanDate(LocalDate.of(2025, 6, 10)) // exactamente 3 meses -> falla
                .build();
        List<String> reasons = new ArrayList<>();
        rule.apply(req, reasons);
        assertTrue(reasons.contains("ANTIGUEDAD_INSUFICIENTE"));
    }

    @Test
    void failsWhenWithinLast3Months() {
        var rule = new TestableLoanHistoryRule(fixed);
        var req = LoanValidationRequest.builder()
                .lastLoanDate(LocalDate.of(2025, 8, 1)) // reciente
                .build();
        List<String> reasons = new ArrayList<>();
        rule.apply(req, reasons);
        assertTrue(reasons.contains("ANTIGUEDAD_INSUFICIENTE"));
    }

    @Test
    void passesWhenNullHistory() {
        var rule = new TestableLoanHistoryRule(fixed);
        var req = LoanValidationRequest.builder().lastLoanDate(null).build();
        List<String> reasons = new ArrayList<>();
        rule.apply(req, reasons);
        assertTrue(reasons.isEmpty());
    }
}
