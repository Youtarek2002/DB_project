package com.databaseproject.parkingproject.controller;

import com.databaseproject.parkingproject.dao.ParkingSpotDao;
import com.databaseproject.parkingproject.dao.ReservationDao;
import com.databaseproject.parkingproject.dto.ResponseMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authenticate/spot")
@RequiredArgsConstructor
@CrossOrigin
public class SpotController {
    private final ParkingSpotDao parkingSpotDao;
    @PutMapping("/update-spot-status")
    public ResponseMessageDto updateSpotStatus(@RequestParam int spotId, @RequestParam String status) {
        parkingSpotDao.updateParkingSpotStatus(spotId, status);
        return new ResponseMessageDto("Spot status updated successfully.",true,200,null);
    }

}