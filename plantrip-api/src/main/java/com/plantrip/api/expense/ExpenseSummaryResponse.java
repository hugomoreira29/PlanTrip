package com.plantrip.api.expense;

import java.math.BigDecimal;
import java.util.Map;

public record ExpenseSummaryResponse(String currency, BigDecimal total, Map<ExpenseCategory, BigDecimal> byCategory) {
}
