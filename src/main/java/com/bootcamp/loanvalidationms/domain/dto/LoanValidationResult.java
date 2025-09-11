package com.bootcamp.loanvalidationms.domain.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import jakarta.validation.constraints.DecimalMin;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanValidationResult {

  private boolean eligible;

  @Singular
  private List<String> reasons;

  @DecimalMin("0.0")
  private BigDecimal monthlyPayment;
}
