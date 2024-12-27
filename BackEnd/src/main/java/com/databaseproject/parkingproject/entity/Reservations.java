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
@AllArgsConstructor
@NoArgsConstructor
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
    private  int cost;


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
                ", cost=" + cost +
                '}';
    }
}
