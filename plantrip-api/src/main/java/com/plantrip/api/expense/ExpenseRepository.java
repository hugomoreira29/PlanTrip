package com.plantrip.api.expense;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByTripIdOrderByAmountDesc(Long tripId);

    Long Id(Long id);
}
