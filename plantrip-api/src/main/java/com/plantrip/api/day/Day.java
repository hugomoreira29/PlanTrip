package com.plantrip.api.day;

import com.plantrip.api.trip.Trip;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "days")
public class Day {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @Column(nullable = false)
    private LocalDate dayDate;

    public Day(Trip trip, LocalDate dayDate) {
        this.trip = trip;
        this.dayDate = dayDate;
    }

    protected Day() {}

    public Long getId() {
        return id;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public LocalDate getDayDate() {
        return dayDate;
    }

    public void setDayDate(LocalDate dayDate) {
        this.dayDate = dayDate;
    }
}
