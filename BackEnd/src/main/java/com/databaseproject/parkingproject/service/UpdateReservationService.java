package com.databaseproject.parkingproject.service;

import com.databaseproject.parkingproject.entity.Reservations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

@Service
public class UpdateReservationService {
    @Scheduled(fixedRate= 1000)
    private void performReservationUpdate() {
        // delete all reservations that have passed their end time
        String url = "jdbc:mysql://localhost:3306/parkingdatabase";
        String user = "root";
        String password = "youssef1234#";
        String query = "DELETE FROM reservations WHERE end_time < NOW()";
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
