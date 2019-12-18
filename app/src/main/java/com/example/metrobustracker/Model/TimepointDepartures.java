package com.example.metrobustracker.Model;

import java.time.LocalDateTime;

public class TimepointDepartures {

    String Description;
    String DepartureTime;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDepartureTime() {
        return DepartureTime;
    }

    public void setDepartureTime(String departureTime) {
        DepartureTime = departureTime;
    }

    @Override
    public String toString() {
        return "TimepointDepartures{" +
                "Description='" + Description + '\'' +
                ", DepartureTime=" + DepartureTime +
                '}';
    }
}
