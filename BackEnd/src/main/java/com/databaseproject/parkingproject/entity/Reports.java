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
public class Reports {
    private Integer id;
    private Integer occupancy;
    private String violation;
    private Integer revenue;
    private Integer parkingSpotId;

    public Reports(Integer id, Integer occupancy, String violation, Integer revenue, Integer parkingSpotId) {
        this.id = id;
        this.occupancy = occupancy;
        this.violation = violation;
        this.revenue = revenue;
        this.parkingSpotId = parkingSpotId;
    }

    public Reports() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(Integer occupancy) {
        this.occupancy = occupancy;
    }

    public String getViolation() {
        return violation;
    }

    public void setViolation(String violation) {
        this.violation = violation;
    }

    public Integer getRevenue() {
        return revenue;
    }

    public void setRevenue(Integer revenue) {
        this.revenue = revenue;
    }

    public Integer getParkingSpotId() {
        return parkingSpotId;
    }

    public void setParkingSpotId(Integer parkingSpotId) {
        this.parkingSpotId = parkingSpotId;
    }

    @Override
    public String toString() {
        return "Reports{" +
                "id=" + id +
                ", occupancy=" + occupancy +
                ", violation='" + violation + '\'' +
                ", revenue=" + revenue +
                ", parkingSpotId=" + parkingSpotId +
                '}';
    }
}
