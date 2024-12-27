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

    @JsonProperty("disabled_price")
    private Integer disabledPrice;

    @JsonProperty("regular_price")
    private Integer regularPrice;

    @JsonProperty("EV_price")
    private Integer EVPrice;

    @JsonProperty("location")
    private String location;

    @JsonProperty("latitude")
    private Double latitude;

    @JsonProperty("revenue")
    private Integer revenue;

    @JsonProperty("longitude")
    private Double longitude;

    @JsonProperty("parking_lot_manager")
    private Integer managerId;

    @JsonProperty("admitted")
    private Boolean admitted;

    // Default constructor
    public ParkingLots() {
        this.admitted = false;
        this.revenue = 0;
        // Set default value for admitted
    }

    // Constructor to initialize all fields (except 'admitted' since it has a default value)
    public ParkingLots(Integer id, Integer disabledCount, Integer regularCount, Integer EVCount, String location, Integer managerId, Double latitude, Double longitude) {
        this.id = id;
        this.disabledCount = disabledCount;
        this.regularCount = regularCount;
        this.EVCount = EVCount;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.managerId = managerId;
        this.admitted = false;  // default value for 'admitted'
        this.revenue = 0;
    }

    @Override
    public String toString() {
        return "ParkingLots{" +
                "id=" + id +
                ", disabledCount=" + disabledCount +
                ", regularCount=" + regularCount +
                ", EVCount=" + EVCount +
                ", disabledPrice=" + disabledPrice +
                ", regularPrice=" + regularPrice +
                ", EVPrice=" + EVPrice +
                ", location='" + location + '\'' +
                ", latitude=" + latitude +
                ", revenue=" + revenue +
                ", longitude=" + longitude +
                ", managerId=" + managerId +
                ", admitted=" + admitted +
                '}';
    }
}
