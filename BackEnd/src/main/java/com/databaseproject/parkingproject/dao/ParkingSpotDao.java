package com.databaseproject.parkingproject.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ParkingSpotDao {
    private static final String SQL_UPDATE_SPOT_STATUS = "UPDATE parking_spots SET status = ? WHERE id = ?";
    private static final String SQL_MARK_AVAILABLE_EXPIRED_RESERVATIONS =
            "UPDATE parking_spots ps " +
                    "SET ps.status = 'AVAILABLE' " +
                    "WHERE ps.id IN ( " +
                    "    SELECT DISTINCT r.parking_spot_id " +
                    "    FROM reservations r " +
                    "    WHERE r.end_time < NOW() AND r.penalty > 0" +
                    ");";
    private static final String SQL_UPDATE_ALL_SPOTS_EXCEPT =
            "UPDATE parking_spots " +
                    "SET status = ? " +
                    "WHERE id NOT IN (%s)";
    private final JdbcTemplate jdbcTemplate;

    public void markAvailableForExpiredReservations() {
        jdbcTemplate.update(SQL_MARK_AVAILABLE_EXPIRED_RESERVATIONS);
    }

    public ParkingSpotDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void updateParkingSpotStatus(int spotId, String status) {
        jdbcTemplate.update(SQL_UPDATE_SPOT_STATUS, status, spotId);
        System.out.println("id"+spotId+"status"+status);

    }
    public void updateAllSpotsExcept(List<Integer> reservedSpotIds, String status) {
        if (reservedSpotIds == null || reservedSpotIds.isEmpty()) {
            System.out.println("Reserved spot IDs list cannot be null or empty.");
            return;
        }

        // Generate placeholders for the IN clause based on the number of IDs in the list
        String placeholders = reservedSpotIds.stream()
                .map(id -> "?")
                .collect(Collectors.joining(", "));

        // Build the final SQL query with the placeholders
        String sql = String.format(SQL_UPDATE_ALL_SPOTS_EXCEPT, placeholders);

        // Prepare the parameters for the update query
        Object[] params = new Object[reservedSpotIds.size() + 1];
        params[0] = status;
        for (int i = 0; i < reservedSpotIds.size(); i++) {
            params[i + 1] = reservedSpotIds.get(i);
        }

        // Execute the update query
        jdbcTemplate.update(sql, params);
    }


}
