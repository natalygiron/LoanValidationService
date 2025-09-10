package com.bootcamp.loanvalidationms.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanValidationRequest {

    @NotNull(message = "El salario mensual es obligatorio")
    @DecimalMin(value = "0.01", message = "El salario mensual debe ser mayor a 0")
    private BigDecimal monthlySalary;

    @NotNull(message = "El monto solicitado es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto solicitado debe ser mayor a 0")
    private BigDecimal requestedAmount;

    @NotNull(message = "El plazo en meses es obligatorio")
    @Min(value = 1, message = "El plazo mínimo es de 1 mes")
    @Max(value = 36, message = "El plazo máximo es de 36 meses")
    private Integer termMonths;

    @PastOrPresent(message = "La fecha del último préstamo no puede ser futura")
    private LocalDate lastLoanDate;
}
