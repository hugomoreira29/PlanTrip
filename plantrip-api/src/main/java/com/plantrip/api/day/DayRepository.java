package com.plantrip.api.day;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DayRepository extends JpaRepository<Day, Long> {
    List<Day> findByTripIdOrderByDayDateAsc(Long tripId);
}
