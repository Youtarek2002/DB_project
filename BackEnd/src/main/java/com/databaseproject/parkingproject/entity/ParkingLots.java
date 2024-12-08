package com.databaseproject.parkingproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class ParkingLots {
    private Integer id;
    private Integer disabledCount;
    private Integer regularCount;
    private boolean isHighDemandArea;
    private Integer EVCount;
    private String location;

    public ParkingLots(Integer id, Integer disabledCount, Integer regularCount, Integer EVCount, String location) {
        this.id = id;
        this.disabledCount = disabledCount;
        this.regularCount = regularCount;
        this.EVCount = EVCount;
        this.location = location;
    }

    public ParkingLots() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDisabledCount() {
        return disabledCount;
    }

    public void setDisabledCount(Integer disabledCount) {
        this.disabledCount = disabledCount;
    }

    public Integer getRegularCount() {
        return regularCount;
    }

    public void setRegularCount(Integer regularCount) {
        this.regularCount = regularCount;
    }

    public Integer getEVCount() {
        return EVCount;
    }

    public void setEVCount(Integer EVCount) {
        this.EVCount = EVCount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "ParkingLots{" +
                "id=" + id +
                ", disabledCount=" + disabledCount +
                ", regularCount=" + regularCount +
                ", EVCount=" + EVCount +
                ", location='" + location + '\'' +
                '}';
    }
}
