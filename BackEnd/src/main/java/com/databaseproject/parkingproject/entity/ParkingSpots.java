package com.databaseproject.parkingproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "parking_spots")
public class ParkingSpots {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "status")
    private Status status;
    @Column(name = "type")
    private String type;
    @Column(name = "price")
    private Integer price;
    @ManyToOne
    @JoinColumn(name = "parking_lot_id", referencedColumnName = "id")
    private ParkingLots parkingLot;

    public enum Status {
        OCCUPIED,
        AVAILABLE,
        RESERVED
    }
}
