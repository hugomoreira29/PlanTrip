package com.plantrip.api.expense;

import java.math.BigDecimal;

public record ExpenseResponse(Long id, String currency, BigDecimal amount, ExpenseCategory category,
                              String description, Long tripId) {
    public static ExpenseResponse from(Expense expense) {
        return new ExpenseResponse(expense.getId(), expense.getCurrency(), expense.getAmount(), expense.getCategory(), expense.getDescription(), expense.getTrip().getId());
    }
}
