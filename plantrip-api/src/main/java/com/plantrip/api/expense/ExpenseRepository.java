package com.plantrip.api.expense;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByTripIdOrderByAmountDesc(Long tripId);


    Optional<Expense> findByIdAndTripOwnerEmail(Long id, String email);

    boolean existsByIdAndTripOwnerEmail(Long id, String email);
}
