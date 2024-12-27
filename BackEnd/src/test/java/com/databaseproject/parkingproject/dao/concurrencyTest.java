package com.databaseproject.parkingproject.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.databaseproject.parkingproject.dto.ResponseMessageDto;
import com.databaseproject.parkingproject.entity.Reservations;

@SpringBootTest
class concurrencyTest {

    @Autowired
    private ReservationDao reservationDao;

    @Test
    void testConcurrentReservationsOnSameSpot() throws InterruptedException {
        // Number of concurrent threads
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        // We will collect the results (messages) here.
        List<Future<ResponseMessageDto>> futures = new ArrayList<>();

        // Prepare the same parkingSpotId, same time range
        int parkingSpotId = 1;
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(2);

        for (int i = 0; i < threadCount; i++) {
            final int userId = i + 1;

            // Each task tries to create a reservation for the same spot/time
            Callable<ResponseMessageDto> task = () -> {
                Reservations reservation = new Reservations();
                reservation.setParkingSpotId(parkingSpotId);
                reservation.setUserId(userId);
                reservation.setStartTime(startTime);
                reservation.setEndTime(endTime);
                reservation.setPenalty(0);

                // Attempt the reservation
                return reservationDao.createReservation(reservation);
            };

            Future<ResponseMessageDto> future = executorService.submit(task);
            futures.add(future);
        }

        executorService.shutdown();
        executorService.awaitTermination(30, TimeUnit.SECONDS);

        // Analyze results
        int successCount = 0;
        int failCount = 0;
        for (Future<ResponseMessageDto> future : futures) {
            try {
                ResponseMessageDto response = future.get();
                if (response.isSuccess()) {
                    successCount++;
                } else {
                    failCount++;
                }
                System.out.println("Thread result:" + response.getMessage());
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        // Usually, we expect only 1 success for the exact same spot/time (if it truly prevents overlap)
        System.out.println("Success count: " + successCount);
        System.out.println("Fail count: " + failCount);

        // Optional assertions:
        // For instance, you might expect exactly 1 success if your code strictly disallows any overlap 
        // on the same parking spot for the exact same time range.
        Assertions.assertTrue(successCount <= 1, 
            "At most one reservation should succeed for the same spot/time range if concurrency is locked properly.");
    }
}
