package com.bootcamp.loanvalidationms.service.rules;

import com.bootcamp.loanvalidationms.domain.dto.LoanValidationRequest;
import com.bootcamp.loanvalidationms.service.ValidationRule;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
public class PaymentCapacityRule implements ValidationRule {

    private static final BigDecimal MAX_PAYMENT_RATIO = BigDecimal.valueOf(0.40);

    public BigDecimal computeMonthlyPayment(LoanValidationRequest request) {
        // Asume request validado (termMonths > 0, requestedAmount != null)
        return request.getRequestedAmount()
                .divide(BigDecimal.valueOf(request.getTermMonths()), 2, RoundingMode.HALF_UP);
    }

    @Override
    public Mono<List<String>> validate(LoanValidationRequest request) {
        List<String> reasons = new ArrayList<>();

        if (request.getRequestedAmount() != null
                && request.getTermMonths() != null
                && request.getTermMonths() > 0
                && request.getMonthlySalary() != null) {

            BigDecimal monthlyPayment = computeMonthlyPayment(request);
            BigDecimal maxAllowed = request.getMonthlySalary().multiply(MAX_PAYMENT_RATIO);

            if (monthlyPayment.compareTo(maxAllowed) > 0) {
                reasons.add("CAPACIDAD_INSUFICIENTE: cuota supera 40% del salario");
            }
        }
        return Mono.just(reasons);
    }
}
