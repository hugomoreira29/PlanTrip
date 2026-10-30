package com.plantrip.api.expense;

import com.plantrip.api.day.DayResponse;
import com.plantrip.api.exchange.ExchangeRateService;
import com.plantrip.api.trip.Trip;
import com.plantrip.api.trip.TripRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

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

    public List<ExpenseResponse> findByTripId(Long tripId) {
        if (!tripRepository.existsById(tripId)) {
            throw new
                    ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found");
        }
        return expenseRepository.findByTripIdOrderByAmountDesc(tripId)
                .stream()
                .map(ExpenseResponse::from)
                .toList();
    }

    public ExpenseResponse create(Long tripId, ExpenseRequest expenseRequest) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found"));
        Expense expense = new Expense(expenseRequest.currency().toUpperCase(), expenseRequest.amount(), expenseRequest.category(), expenseRequest.description(), trip);
        Expense saved = expenseRepository.save(expense);
        return ExpenseResponse.from(saved);
    }

    public ExpenseResponse update(Long expenseId, ExpenseRequest expenseRequest) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found"));
        expense.setCurrency(expenseRequest.currency().toUpperCase());
        expense.setAmount(expenseRequest.amount());
        expense.setCategory(expenseRequest.category());
        expense.setDescription(expenseRequest.description());
        Expense saved = expenseRepository.save(expense);
        return ExpenseResponse.from(saved);

    }

    public void delete(Long expenseId) {
        if (!expenseRepository.existsById(expenseId)) {
            throw new
                    ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found");
        }
        expenseRepository.deleteById(expenseId);
    }

    public ExpenseSummaryResponse summary(Long tripId, String currency) {
        if (!tripRepository.existsById(tripId)) {
            throw new
                    ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found");
        }
        List<ExpenseResponse> expenses = expenseRepository.findByTripIdOrderByAmountDesc(tripId)
                .stream()
                .map(ExpenseResponse::from)
                .toList();

        BigDecimal total = BigDecimal.ZERO;
        for (ExpenseResponse expenseResponse : expenses) {
            BigDecimal rate = exchangeRateService.getRate(expenseResponse.currency(), currency);
            BigDecimal converted = rate.multiply(expenseResponse.amount());
            total = total.add(converted);
        }
        total = total.setScale(2, RoundingMode.HALF_EVEN);
        return new ExpenseSummaryResponse(currency,total,null);
    }
}
