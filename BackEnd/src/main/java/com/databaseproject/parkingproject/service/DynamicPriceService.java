package com.databaseproject.parkingproject.service;

import com.databaseproject.parkingproject.entity.ParkingSpots;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class DynamicPriceService {
    private final JdbcTemplate jdbcTemplate;

    public DynamicPriceService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer calculatePrice(ParkingSpots parkingSpots){
        int price=5; //default price
        final LocalTime peakStart = LocalTime.of(14, 0);
        final LocalTime peakEnd = LocalTime.of(19, 0);
        final LocalTime currentTime = LocalTime.now();
        if (currentTime.isAfter(peakStart) && currentTime.isBefore(peakEnd)) {
            price +=10;
        }
        String countQuery="select count(*) from parking_spots where parking_lot_id=? and status='AVAILABLE'";
        int numberOfNonOccupiedSpots=jdbcTemplate.queryForObject(countQuery, Integer.class,parkingSpots.getParkingLotId());
        if(numberOfNonOccupiedSpots<5){
            price+=7;
        }
        String selectQuery="select is_high_demand_area from parking_lots where id=?";
        boolean isHighDemandArea=jdbcTemplate.queryForObject(selectQuery, Boolean.class,parkingSpots.getParkingLotId());
        if(isHighDemandArea){
            price+=10;
        }

        return price;
    }
}
