package com.databaseproject.parkingproject.dao;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationExpiryService {
    private final ReservationDao reservationDao;

    @Scheduled(fixedRate = 60000)
    public void expireReservationsAutomatically() {
        reservationDao.expireReservations();
        System.out.println("Checked and expired reservations if necessary.");
    }
}
