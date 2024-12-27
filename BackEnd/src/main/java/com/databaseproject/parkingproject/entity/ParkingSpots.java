package com.databaseproject.parkingproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter

@SuperBuilder

public class ParkingSpots {
    private Integer id;
    private Status status;
    private String type;
    private Integer price;
    private Integer parkingLotId;
    private Integer revenue;
    private Integer penalty;

    public enum Status {
        OCCUPIED,
        AVAILABLE,
        RESERVED
    }

    public ParkingSpots(Integer id, Status status, String type, Integer price, Integer parkingLotId, Integer revenue, Integer penalty) {
        this.id = id;
        this.status = status;
        this.type = type;
        this.price = price;
        this.parkingLotId = parkingLotId;
        this.revenue = revenue;
        this.penalty = penalty;
    }

    public ParkingSpots() {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(Integer parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public Integer getRevenue() {
        return revenue;
    }

    public void setRevenue(Integer revenue) {
        this.revenue = revenue;
    }

    public Integer getPenalty() {
        return penalty;
    }

    public void setPenalty(Integer penalty) {
        this.penalty = penalty;
    }

    @Override
    public String toString() {
        return "ParkingSpots{" +
                "id=" + id +
                ", status=" + status +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", parkingLotId=" + parkingLotId +
                ", revenue=" + revenue +
                ", penalty=" + penalty +
                '}';
    }
}
