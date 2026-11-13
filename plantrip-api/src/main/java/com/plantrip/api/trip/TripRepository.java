package com.plantrip.api.trip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {
    Optional<Trip> findByIdAndOwnerEmail(Long id, String email);

    @Query("SELECT DISTINCT t FROM Trip t LEFT JOIN t.members m " +
            "WHERE t.owner.email = :email OR m.email = :email")
    List<Trip> findAllAccessible(@Param("email") String email);

    @Query("SELECT COUNT(t) > 0 FROM Trip t LEFT JOIN t.members m " +
            "WHERE t.id = :id AND (t.owner.email = :email OR m.email = :email)")
    boolean canAccess(@Param("id") Long id, @Param("email") String email);

    Optional<Trip> findByShareToken(String shareToken);
}
