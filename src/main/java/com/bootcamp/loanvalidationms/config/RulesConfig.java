package com.bootcamp.loanvalidationms.config;

import com.bootcamp.loanvalidationms.service.LoanValidationService;
import com.bootcamp.loanvalidationms.service.rules.LoanHistoryRule;
import com.bootcamp.loanvalidationms.service.rules.TermMonthsRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.List;

@Configuration
public class RulesConfig {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public LoanHistoryRule loanHistoryRule(Clock clock) {
        return new LoanHistoryRule(clock);
    }

    @Bean
    public TermMonthsRule termMonthsRule() {
        return new TermMonthsRule();
    }

    @Bean
    public LoanValidationService loanValidationService(
            LoanHistoryRule r1,
            TermMonthsRule r2
    ) {
        return new LoanValidationService(List.of(r1, r2));
    }
}
