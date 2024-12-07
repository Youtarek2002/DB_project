package com.databaseproject.parkingproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "parking_lots")
public class ParkingLots {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "disabled_count")
    private Integer disabledCount;
    @Column(name = "regular_count")
    private Integer regularCount;
    @Column(name = "EV_count")
    private Integer EVCount;
    @Column(name = "location")
    private String location;
}
