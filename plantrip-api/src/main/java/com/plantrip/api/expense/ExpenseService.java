package com.plantrip.api.expense;

import com.plantrip.api.exchange.ExchangeRateService;
import com.plantrip.api.trip.Trip;
import com.plantrip.api.trip.TripRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.math.RoundingMode.HALF_UP;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final TripRepository tripRepository;
    private final ExchangeRateService exchangeRateService;

    public ExpenseService(ExpenseRepository expenseRepository, TripRepository tripRepository, ExchangeRateService exchangeRateService) {
        this.expenseRepository = expenseRepository;
        this.tripRepository = tripRepository;
        this.exchangeRateService = exchangeRateService;
    }

    public List<ExpenseResponse> findByTripId(Long tripId, String email) {
        if (!tripRepository.canAccess(tripId, email)) {
            throw new
                    ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found");
        }
        return expenseRepository.findByTripIdOrderByAmountDesc(tripId)
                .stream()
                .map(ExpenseResponse::from)
                .toList();
    }

    public ExpenseResponse create(Long tripId, ExpenseRequest expenseRequest, String email) {
        if (!tripRepository.canAccess(tripId, email)) { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found"); }
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found"));
        Expense expense = new Expense(expenseRequest.currency().toUpperCase(), expenseRequest.amount(), expenseRequest.category(), expenseRequest.description(), trip);
        Expense saved = expenseRepository.save(expense);
        return ExpenseResponse.from(saved);
    }

    public ExpenseResponse update(Long expenseId, ExpenseRequest expenseRequest, String email) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found"));
        if (!tripRepository.canAccess(expense.getTrip().getId(), email)) { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found"); }
        expense.setCurrency(expenseRequest.currency().toUpperCase());
        expense.setAmount(expenseRequest.amount());
        expense.setCategory(expenseRequest.category());
        expense.setDescription(expenseRequest.description());
        Expense saved = expenseRepository.save(expense);
        return ExpenseResponse.from(saved);

    }

    public void delete(Long expenseId, String email) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found"));
        if (!tripRepository.canAccess(expense.getTrip().getId(), email)) { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found"); }
        expenseRepository.delete(expense);
    }

    public ExpenseSummaryResponse summary(Long tripId, String currency, String email) {
        if (!tripRepository.canAccess(tripId, email)) {
            throw new
                    ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found");
        }
        List<ExpenseResponse> expenses = expenseRepository.findByTripIdOrderByAmountDesc(tripId)
                .stream()
                .map(ExpenseResponse::from)
                .toList();

        BigDecimal total = BigDecimal.ZERO;
        Map<ExpenseCategory, BigDecimal> byCategory = new HashMap<>();
        for (ExpenseResponse expenseResponse : expenses) {
            BigDecimal rate = exchangeRateService.getRate(expenseResponse.currency(), currency);
            BigDecimal converted = rate.multiply(expenseResponse.amount());
            total = total.add(converted);
            BigDecimal current = byCategory.getOrDefault(expenseResponse.category(), BigDecimal.ZERO);
            byCategory.put(expenseResponse.category(), current.add(converted));
        }
        total = total.setScale(2, HALF_UP);
        byCategory.replaceAll((category, value) -> value.setScale(2, HALF_UP));
        return new ExpenseSummaryResponse(currency, total, byCategory);
    }
}
