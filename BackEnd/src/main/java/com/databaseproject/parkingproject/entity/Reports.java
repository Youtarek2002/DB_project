package com.databaseproject.parkingproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "reports")
public class Reports {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "occupancy")
    private Integer occupancy;
    @Column(name = "violation")
    private String violation;
    @Column(name = "revenue")
    private Integer revenue;
    @ManyToOne
    @JoinColumn(name = "parking_spot_id", referencedColumnName = "id")
    private ParkingSpots parkingSpot;
}
