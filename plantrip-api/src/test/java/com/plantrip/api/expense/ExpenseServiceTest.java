package com.plantrip.api.expense;

import com.plantrip.api.exchange.ExchangeRateService;
import com.plantrip.api.trip.Trip;
import com.plantrip.api.trip.TripRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTest {
    @Mock
    private ExpenseRepository expenseRepository;
    @Mock
    private TripRepository tripRepository;
    @Mock
    private ExchangeRateService exchangeRateService;
    @InjectMocks
    private ExpenseService expenseService;

    @Test
    void summaryConvertsAndGroupsByCategory(){
        Trip trip = new Trip("New York","New York", LocalDate.of(2026,11,1),LocalDate.of(2026,11,7));
        Expense dinner = new Expense("USD",new BigDecimal("50.00"),ExpenseCategory.FOOD,"Restaurant in New York",trip);
        Expense metro = new Expense("USD",new BigDecimal("18.00"),ExpenseCategory.TRANSPORT,"Metro from Canal St to Cathedral Pkwy (110 St)",trip);

        when(tripRepository.existsById(1L)).thenReturn(true);
        when(expenseRepository.findByTripIdOrderByAmountDesc(1L)).thenReturn(List.of(dinner,metro));
        when(exchangeRateService.getRate("USD","EUR")).thenReturn(new BigDecimal("0.9"));

        ExpenseSummaryResponse result = expenseService.summary(1L, "EUR");

        assertEquals(new BigDecimal("61.20"), result.total());
        assertEquals(new BigDecimal("45.00"),result.byCategory().get(ExpenseCategory.FOOD));
        assertEquals(new BigDecimal("16.20"),result.byCategory().get(ExpenseCategory.TRANSPORT));
    }
}
