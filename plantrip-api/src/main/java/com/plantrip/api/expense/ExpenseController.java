package com.plantrip.api.expense;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("/trips/{tripId}/expenses")
    public List<ExpenseResponse> findByTripId(@PathVariable Long tripId) {
        return expenseService.findByTripId(tripId);
    }

    @PostMapping("/trips/{tripId}/expenses")
    @ResponseStatus(HttpStatus.CREATED)
    public ExpenseResponse create(@Valid @RequestBody ExpenseRequest expenseRequest, @PathVariable Long tripId) {
        return expenseService.create(tripId, expenseRequest);
    }

    @PutMapping("/expenses/{id}")
    public ExpenseResponse update(@PathVariable Long id, @Valid @RequestBody ExpenseRequest expenseRequest) {
        return expenseService.update(id, expenseRequest);
    }

    @DeleteMapping("/expenses/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        expenseService.delete(id);
    }
}
