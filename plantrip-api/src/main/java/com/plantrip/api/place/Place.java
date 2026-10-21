package com.plantrip.api.place;

import com.plantrip.api.day.Day;
import jakarta.persistence.*;

@Entity
@Table(name= "places")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "day_id")
    private Day day;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 150)
    private String description;

    public Place(Day day, String name, String description) {
        this.day = day;
        this.name = name;
        this.description = description;
    }

    protected Place() {}

    public Long getId() {
        return id;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
