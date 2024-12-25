package com.databaseproject.parkingproject.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ParkingSpotDao {
    private static final String SQL_UPDATE_SPOT_STATUS = "UPDATE parking_spots SET status = ? WHERE id = ?";
    private static final String SQL_MARK_AVAILABLE_EXPIRED_RESERVATIONS =
            "UPDATE parking_spots ps \n" +
                    "SET ps.status = 'AVAILABLE' \n" +
                    "WHERE ps.status = 'RESERVED' \n" +
                    "AND ps.id IN (\n" +
                    "    SELECT DISTINCT r.parking_spot_id\n" +
                    "    FROM reservations r\n" +
                    "    INNER JOIN (\n" +
                    "        SELECT parking_spot_id, MAX(id) AS last_reservation_id\n" +
                    "        FROM reservations\n" +
                    "        GROUP BY parking_spot_id\n" +
                    "    ) latest_reservations\n" +
                    "    ON r.id = latest_reservations.last_reservation_id\n" +
                    "    WHERE r.penalty > 0 AND start_time > NOW()" +
                    ");";
    private static final String SQL_UPDATE_ALL_SPOTS_EXCEPT =
            "UPDATE parking_spots " +
                    "SET status = ? " +
                    "WHERE id NOT IN (%s) AND status != 'OCCUPIED'";
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

        String placeholders = reservedSpotIds.stream()
                .map(id -> "?")
                .collect(Collectors.joining(", "));

        String sql = String.format(SQL_UPDATE_ALL_SPOTS_EXCEPT, placeholders);

        Object[] params = new Object[reservedSpotIds.size() + 1];
        params[0] = status;
        for (int i = 0; i < reservedSpotIds.size(); i++) {
            params[i + 1] = reservedSpotIds.get(i);
        }
        jdbcTemplate.update(sql, params);
    }


}
