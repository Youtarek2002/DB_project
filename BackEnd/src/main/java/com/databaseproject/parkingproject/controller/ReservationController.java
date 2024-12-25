package com.databaseproject.parkingproject.controller;

import com.databaseproject.parkingproject.dao.ReservationDao;
import com.databaseproject.parkingproject.dto.ResponseMessageDto;
import com.databaseproject.parkingproject.entity.ParkingSpots;
import com.databaseproject.parkingproject.entity.Reservations;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/authenticate/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationDao reservationDao;

    @PostMapping("/create")
    public ResponseMessageDto createReservation(@RequestBody Reservations reservation) {
        return reservationDao.createReservation(reservation);
    }

    @DeleteMapping("/delete")
    public ResponseMessageDto deleteReservation(@RequestParam int id) {
        return reservationDao.deleteReservation(id);
    }

    @PutMapping("/update")
    public ResponseMessageDto updateReservation(@RequestBody Reservations reservation) {
        return reservationDao.updateReservation(reservation);
    }

    @GetMapping("/all")
    public List<Reservations> getAllReservations() {
        return reservationDao.getAllReservations();
    }

    @GetMapping("/available-spots")
    public List<ParkingSpots> getAvailableSpots(
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam int lotId) {

        return reservationDao.getAvailableSpots(startTime, endTime, lotId);
    }


    @GetMapping("/user-reservations")
    public List<Reservations> getUserReservations(@RequestParam int userId) {
        return reservationDao.getUserReservations(userId);
    }

    @GetMapping("/parking-lot-reservations")
    public List<Reservations> getParkingLotReservations(@RequestParam int parkingLotId) {
        return reservationDao.getParkingLotReservations(parkingLotId);
    }
    @GetMapping("/expired")
    public ResponseMessageDto expireReservations() {
        return reservationDao.expireReservations();
    }
    @GetMapping("/available-spots-from-now")
    public List<ParkingSpots> getAvailableSpotsFromNow(@RequestParam String endTime ,@RequestParam int lotId) {
        String startTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return reservationDao.getAvailableSpots(startTime, endTime,lotId);
    }
    @GetMapping("/total-penalty")
    public int getTotalPenalty(@RequestParam int userId) {
        int totalPenalty = reservationDao.getTotalPenaltyForUser(userId);
        return totalPenalty;
    }


}