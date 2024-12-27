package com.databaseproject.parkingproject.service;

import com.databaseproject.parkingproject.config.WebSocketHandler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Service
public class ParkingSpotStatusService {

    private final WebSocketHandler webSocketHandler;

    public ParkingSpotStatusService(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Scheduled(fixedRate = 1000) // Poll every second
    public void processStatusChanges() {
        String url = "jdbc:mysql://localhost:3306/parkingdatabase";
        String user = "root";
        String password = "youssef1234#";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT id, parking_spot_id, old_status, new_status FROM parking_spot_changes";
            try (PreparedStatement stmt = connection.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                if (!rs.isBeforeFirst()) {
                    return; // Exit if there are no rows
                }
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int spotId = rs.getInt("parking_spot_id");
                    String oldStatus = rs.getString("old_status");
                    String newStatus = rs.getString("new_status");

                    // Notify WebSocket handler
                    String message = "Spot " + spotId + " changed from " + oldStatus + " to " + newStatus;
                    webSocketHandler.broadcastMessage(message);

                    // Remove the processed entry
                    String deleteQuery = "DELETE FROM parking_spot_changes WHERE id = ?";
                    try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
                        deleteStmt.setInt(1, id);
                        deleteStmt.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }
}
