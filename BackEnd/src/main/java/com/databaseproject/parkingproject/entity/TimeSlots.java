package com.databaseproject.parkingproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.sql.In;


@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
public class TimeSlots {
    private Integer parkingSpotId;
    private ParkingSpots.Status status;
    private LocalDateTime startTime;

    public Integer getParkingSpotId() {
        return parkingSpotId;
    }

    public void setParkingSpotId(Integer parkingSpotId) {
        this.parkingSpotId = parkingSpotId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public ParkingSpots.Status getStatus() {
        return status;
    }

    public void setStatus(ParkingSpots.Status status) {
        this.status = status;
    }
}
