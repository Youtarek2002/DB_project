package com.databaseproject.parkingproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

public class Notifications {
    private Integer id;
    private Status status;
    private Time time;
    private String message;
    private Integer userId;

    public enum Status {
        PENDING,
        SENT,
        FAILED,
        DELIVERED
    }

    public Notifications(Integer id, Status status, Time time, String message, Integer userId) {
        this.id = id;
        this.status = status;
        this.time = time;
        this.message = message;
        this.userId = userId;
    }

    public Notifications() {
    }

    @Override
    public String toString() {
        return "Notifications{" +
                "id=" + id +
                ", status=" + status +
                ", time=" + time +
                ", message='" + message + '\'' +
                ", userId=" + userId +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
