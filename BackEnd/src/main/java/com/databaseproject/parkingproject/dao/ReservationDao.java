package com.databaseproject.parkingproject.dao;

import com.databaseproject.parkingproject.dto.ResponseMessageDto;
import com.databaseproject.parkingproject.entity.ParkingSpots;
import com.databaseproject.parkingproject.entity.Reservations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public class ReservationDao {
    private static final String SQL_CHECK_SPOT_AVAILABILITY = "SELECT COUNT(*) FROM reservations WHERE parking_spot_id = ? AND start_time < ? AND end_time > ?;";
    private static final String SQL_INSERT_RESERVATION = "INSERT INTO reservations (parking_spot_id, user_id, start_time, end_time, penalty) VALUES (?, ?, ?, ?, ?);";
    private static final String SQL_DELETE_RESERVATION = "DELETE FROM reservations WHERE id = ?;";
    private static final String SQL_UPDATE_RESERVATION = "UPDATE reservations SET parking_spot_id = ?, user_id = ?, start_time = ?, end_time = ?, penalty = ? WHERE id = ?;";
    private static final String SQL_GET_ALL_RESERVATIONS = "SELECT id, penalty, start_time, end_time, duration, user_id, parking_spot_id, transaction_id FROM reservations;";
    private static final String SQL_GET_USER_RESERVATIONS = "SELECT * FROM reservations WHERE user_id = ?;";
    private static final String SQL_GET_PARKING_LOT_RESERVATIONS = "SELECT r.* FROM reservations r JOIN parking_spots ps ON r.parking_spot_id = ps.id WHERE ps.parking_lot_id = ?;";
    private static final String SQL_MARK_EXPIRED_RESERVATIONS = "UPDATE reservations SET penalty = penalty + 50 WHERE end_time < NOW() AND penalty = 0;";
    private static final String SQL_GET_AVAILABLE_SPOTS = "SELECT * FROM parking_spots ps " +
            "WHERE ps.parking_lot_id = ? " +
            "AND ps.id NOT IN (SELECT rs.parking_spot_id FROM reservations rs " +
            "WHERE rs.start_time < ? AND rs.end_time > ?)";
    private static final String SQL_USER_PENALTY = "SELECT SUM(penalty) FROM reservations WHERE user_id = ?";

    private final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ResponseMessageDto createReservation(Reservations reservation) {
        int rowsAffected = jdbcTemplate.update(SQL_INSERT_RESERVATION,
                reservation.getParkingSpotId(),
                reservation.getUserId(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getPenalty());
        return new ResponseMessageDto(
                rowsAffected == 1 ? "Reservation created successfully" : "Error creating reservation",
                rowsAffected == 1,
                rowsAffected == 1 ? 200 : 500,
                null);
    }

    public ResponseMessageDto deleteReservation(int id) {
        int rowsAffected = jdbcTemplate.update(SQL_DELETE_RESERVATION, id);
        return new ResponseMessageDto(
                rowsAffected == 1 ? "Reservation deleted successfully" : "Error deleting reservation",
                rowsAffected == 1,
                rowsAffected == 1 ? 200 : 500,
                null);
    }

    public ResponseMessageDto updateReservation(Reservations reservation) {
        if (isSpotAlreadyBooked(reservation.getParkingSpotId(), reservation.getStartTime(), reservation.getEndTime())) {
            return new ResponseMessageDto("Parking spot already reserved for the selected time.", false, 400, null);
        }
        int rowsAffected = jdbcTemplate.update(SQL_UPDATE_RESERVATION,
                reservation.getParkingSpotId(),
                reservation.getUserId(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getPenalty(),
                reservation.getId());
        return new ResponseMessageDto(
                rowsAffected == 1 ? "Reservation updated successfully" : "Error updating reservation",
                rowsAffected == 1,
                rowsAffected == 1 ? 200 : 500,
                null);
    }

    public List<Reservations> getAllReservations() {
        return jdbcTemplate.query(SQL_GET_ALL_RESERVATIONS, new ReservationRowMapper());
    }

    public List<ParkingSpots> getAvailableSpots(String startTime, String endTime, int lotId) {
        return jdbcTemplate.query(SQL_GET_AVAILABLE_SPOTS, new Object[]{lotId, endTime, startTime}, new ParkingSpotRowMapper());
    }


    public List<Reservations> getUserReservations(int userId) {
        return jdbcTemplate.query(SQL_GET_USER_RESERVATIONS, new Object[]{userId}, new ReservationRowMapper());
    }

    public List<Reservations> getParkingLotReservations(int parkingLotId) {
        return jdbcTemplate.query(SQL_GET_PARKING_LOT_RESERVATIONS, new Object[]{parkingLotId}, new ReservationRowMapper());
    }

    private boolean isSpotAlreadyBooked(int parkingSpotId, LocalDateTime startTime,LocalDateTime  endTime) {
        Integer count = jdbcTemplate.queryForObject(SQL_CHECK_SPOT_AVAILABILITY, new Object[]{parkingSpotId, endTime, startTime}, Integer.class);
        return count != null && count > 0;
    }
    public ResponseMessageDto expireReservations() {
        int rowsAffected = jdbcTemplate.update(SQL_MARK_EXPIRED_RESERVATIONS);
        return new ResponseMessageDto(
                rowsAffected > 0 ? "Expired reservations updated successfully" : "No reservations to expire",
                rowsAffected > 0,
                rowsAffected > 0 ? 200 : 204,
                null);
    }

    public class ReservationRowMapper implements RowMapper<Reservations> {

        @Override
        public Reservations mapRow(ResultSet rs, int rowNum) throws SQLException {
            Reservations reservation = new Reservations();
            reservation.setId(rs.getInt("id"));

            int penalty = rs.getInt("penalty");
            if (rs.wasNull()) {
                penalty = 0;
            }
            reservation.setPenalty(penalty);

            reservation.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
            reservation.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());

            String durationStr = rs.getString("duration");
            if (durationStr != null && !durationStr.isEmpty()) {
                try {
                    reservation.setDuration(Duration.parse(durationStr));
                } catch (Exception e) {
                    reservation.setDuration(Duration.ZERO);
                }
            } else {
                reservation.setDuration(Duration.ZERO);
            }

            reservation.setUserId(rs.getInt("user_id"));
            reservation.setParkingSpotId(rs.getInt("parking_spot_id"));
            reservation.setTransactionId(rs.getInt("transaction_id"));

            return reservation;
        }
    }


    public class ParkingSpotRowMapper implements RowMapper<ParkingSpots> {
        @Override
        public ParkingSpots mapRow(ResultSet rs, int rowNum) throws SQLException {
            ParkingSpots parkingSpot = new ParkingSpots();
            parkingSpot.setId(rs.getInt("id"));
            parkingSpot.setType(rs.getString("type"));
            parkingSpot.setPrice(rs.getInt("price"));
            parkingSpot.setParkingLotId(rs.getInt("parking_lot_id"));
            return parkingSpot;
        }
    }
    public int getTotalPenaltyForUser(int userId) {
        Integer totalPenalty = jdbcTemplate.queryForObject(SQL_USER_PENALTY, new Object[]{userId}, Integer.class);
        return totalPenalty != null ? totalPenalty : 0;
    }


}
