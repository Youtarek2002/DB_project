package com.databaseproject.parkingproject.dao;
import com.databaseproject.parkingproject.entity.Reservations;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ParkingSpotStatusScheduler {

    private final ReservationDao reservationDao;
    private final ParkingSpotDao parkingSpotDao;

    @Scheduled(fixedRate = 1000)
    public void updateParkingSpotStatuses() {
        LocalDateTime now = LocalDateTime.now();

        List<Reservations> currentReservations = reservationDao.getReservationsByTime(now);

        for (Reservations reservation : currentReservations) {
            parkingSpotDao.updateParkingSpotStatus(reservation.getParkingSpotId(), "RESERVED");
            System.out.println("reserved");
            System.out.println(reservation.toString());
        }

        List<Integer> reservedSpotIds = currentReservations.stream()
                .map(Reservations::getParkingSpotId)
                .toList();

        parkingSpotDao.updateAllSpotsExcept(reservedSpotIds, "AVAILABLE");
    }
}
