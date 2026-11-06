package com.plantrip.api.expense;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
    public List<ExpenseResponse> findByTripId(@AuthenticationPrincipal Jwt jwt, @PathVariable Long tripId) {
        return expenseService.findByTripId(tripId, jwt.getSubject());
    }

    @PostMapping("/trips/{tripId}/expenses")
    @ResponseStatus(HttpStatus.CREATED)
    public ExpenseResponse create(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody ExpenseRequest expenseRequest, @PathVariable Long tripId) {
        return expenseService.create(tripId, expenseRequest, jwt.getSubject());
    }

    @PutMapping("/expenses/{id}")
    public ExpenseResponse update(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id, @Valid @RequestBody ExpenseRequest expenseRequest) {
        return expenseService.update(id, expenseRequest, jwt.getSubject());
    }

    @DeleteMapping("/expenses/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        expenseService.delete(id, jwt.getSubject());
    }

    @GetMapping("/trips/{tripId}/expenses/summary")
    public ExpenseSummaryResponse summary(@AuthenticationPrincipal Jwt jwt, @PathVariable Long tripId, @RequestParam String currency) {
        return expenseService.summary(tripId, currency, jwt.getSubject());
    }
}
