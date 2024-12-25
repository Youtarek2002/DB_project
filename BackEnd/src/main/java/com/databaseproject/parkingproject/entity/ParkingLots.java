package com.databaseproject.parkingproject.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ParkingLots {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("disabled_count")
    private Integer disabledCount;

    @JsonProperty("regular_count")
    private Integer regularCount;

    @JsonProperty("EV_count")
    private Integer EVCount;

    @JsonProperty("location")
    private String location;

    @JsonProperty("latitude")
    private Double latitude;

    @JsonProperty("longitude")
    private Double longitude;

    @JsonProperty("parking_lot_manager")
    private Integer managerId;

    @JsonProperty("admitted")
    private Boolean admitted;

    // Default constructor
    public ParkingLots() {
        this.admitted = false; // Set default value for admitted
    }

    // Constructor to initialize all fields (except 'admitted' since it has a default value)
    public ParkingLots(Integer id, Integer disabledCount, Integer regularCount, Integer EVCount, String location, Double latitude, Double longitude, Integer managerId) {
        this.id = id;
        this.disabledCount = disabledCount;
        this.regularCount = regularCount;
        this.EVCount = EVCount;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.managerId = managerId;
        this.admitted = false;
    }

    @Override
    public String toString() {
        return "ParkingLots{" +
                "admitted=" + admitted +
                ", id=" + id +
                ", disabledCount=" + disabledCount +
                ", regularCount=" + regularCount +
                ", EVCount=" + EVCount +
                ", location='" + location + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", managerId=" + managerId +
                '}';
    }
}
