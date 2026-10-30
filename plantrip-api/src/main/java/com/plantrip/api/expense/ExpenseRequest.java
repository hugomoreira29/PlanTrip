package com.plantrip.api.expense;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ExpenseRequest(
        @NotBlank @Size(min = 3, max = 3) String currency,
        @NotNull @Positive BigDecimal amount,
        @NotNull  ExpenseCategory category,
        @Size(max = 50) String description
) {
}
