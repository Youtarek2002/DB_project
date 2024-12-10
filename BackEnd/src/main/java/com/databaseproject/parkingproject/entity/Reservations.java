package com.databaseproject.parkingproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter

@SuperBuilder
public class Reservations {
    private Integer id;
    private Integer penalty;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Duration duration;
    private Integer userId;
    private Integer parkingSpotId;
    private Integer transactionId;

    public Reservations(Integer id, Integer penalty, LocalDateTime startTime, LocalDateTime endTime, Duration duration, Integer userId, Integer parkingSpotId, Integer transactionId) {
        this.id = id;
        this.penalty = penalty;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.userId = userId;
        this.parkingSpotId = parkingSpotId;
        this.transactionId = transactionId;
    }

    public Reservations() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPenalty() {
        return penalty;
    }

    public void setPenalty(Integer penalty) {
        this.penalty = penalty;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getParkingSpotId() {
        return parkingSpotId;
    }

    public void setParkingSpotId(Integer parkingSpotId) {
        this.parkingSpotId = parkingSpotId;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "Reservations{" +
                "id=" + id +
                ", penalty=" + penalty +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", duration=" + duration +
                ", userId=" + userId +
                ", parkingSpotId=" + parkingSpotId +
                ", transactionId=" + transactionId +
                '}';
    }
}
