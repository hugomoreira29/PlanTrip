package com.plantrip.api.trip;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByOwnerEmail(String email);

    Optional<Trip> findByIdAndOwnerEmail(Long id, String email);

    boolean existsByIdAndOwnerEmail(Long id, String email);
}
