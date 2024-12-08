package com.databaseproject.parkingproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class ParkingSpots {
    private Integer id;
    private Status status;
    private String type;
    private Integer price;
    private Integer parkingLotId;

    public enum Status {
        OCCUPIED,
        AVAILABLE,
        RESERVED
    }

    public ParkingSpots(Integer id, Status status, String type, Integer price, Integer parkingLotId) {
        this.id = id;
        this.status = status;
        this.type = type;
        this.price = price;
        this.parkingLotId = parkingLotId;
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

    @Override
    public String toString() {
        return "ParkingSpots{" +
                "id=" + id +
                ", status=" + status +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", parkingLotId=" + parkingLotId +
                '}';
    }
}
