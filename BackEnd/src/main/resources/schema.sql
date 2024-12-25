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

    private static final String SQL_INSERT_RESERVATION = "INSERT INTO reservations (parking_spot_id, user_id, start_time, end_time, penalty) VALUES (?, ?, ?, ?, ?);";
    private static final String SQL_DELETE_RESERVATION = "DELETE FROM reservations WHERE id = ?;";
    private static final String SQL_UPDATE_RESERVATION = "UPDATE reservations SET parking_spot_id = ?, user_id = ?, start_time = ?, end_time = ?, penalty = ? WHERE id = ?;";
    private static final String SQL_GET_ALL_RESERVATIONS = "SELECT id, penalty, start_time, end_time, duration, user_id, parking_spot_id, transaction_id FROM reservations;";
    private static final String SQL_GET_USER_RESERVATIONS = "SELECT * FROM reservations WHERE user_id = ?;";
    private static final String SQL_MARK_EXPIRED_RESERVATIONS = "UPDATE reservations SET penalty = penalty + 50 WHERE end_time < NOW() AND penalty = 0;";
    private static final String SQL_GET_AVAILABLE_SPOTS = "SELECT ps.id, ps.type, ps.price, ps.parking_lot_id " +
            "FROM parking_spots ps " +
            "WHERE ps.parking_lot_id = ? " +
            "AND ps.id NOT IN (" +
            "    SELECT ts.parking_spot_id " +
            "    FROM time_slots ts " +
            "    WHERE ts.start_time BETWEEN ? AND ? " +
            "    AND ts.status != 'AVAILABLE'" +
            ");";
    private static final String SQL_USER_PENALTY = "SELECT SUM(penalty) FROM reservations WHERE user_id = ?";

    private final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ResponseMessageDto createReservation(Reservations reservation) {
        // Insert the reservation into the reservations table
        int rowsAffected = jdbcTemplate.update(SQL_INSERT_RESERVATION,
                reservation.getParkingSpotId(),
                reservation.getUserId(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getPenalty());

        if (rowsAffected == 1) {
            // Update the time_slots table for the parking spot during the reservation period
            createTimeSlotsForReservation(reservation.getParkingSpotId(), reservation.getStartTime(), reservation.getEndTime(), "RESERVED");
            return new ResponseMessageDto("Reservation created successfully", true, 200, null);
        } else {
            return new ResponseMessageDto("Error creating reservation", false, 500, null);
        }
    }

    public ResponseMessageDto deleteReservation(int id) {
        // Retrieve the reservation details to delete corresponding time slots
        Reservations reservation = getReservationById(id);
        if (reservation != null) {
            // Delete the reservation from the reservations table
            int rowsAffected = jdbcTemplate.update(SQL_DELETE_RESERVATION, id);

            if (rowsAffected == 1) {
                // Reset time slots status to AVAILABLE after reservation is deleted
                updateTimeSlotsForReservation(reservation.getParkingSpotId(), reservation.getStartTime(), reservation.getEndTime(), "AVAILABLE");
                return new ResponseMessageDto("Reservation deleted successfully", true, 200, null);
            }
        }
        return new ResponseMessageDto("Error deleting reservation", false, 500, null);
    }

    public ResponseMessageDto updateReservation(Reservations reservation) {
        // First, check if the parking spot is available
        if (isSpotAlreadyBooked(reservation.getParkingSpotId(), reservation.getStartTime(), reservation.getEndTime())) {
            return new ResponseMessageDto("Parking spot already reserved for the selected time.", false, 400, null);
        }

        // Update reservation details in the database
        int rowsAffected = jdbcTemplate.update(SQL_UPDATE_RESERVATION,
                reservation.getParkingSpotId(),
                reservation.getUserId(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getPenalty(),
                reservation.getId());

        if (rowsAffected == 1) {
            // Update time slots status to reflect the new reservation
            updateTimeSlotsForReservation(reservation.getParkingSpotId(), reservation.getStartTime(), reservation.getEndTime(), "RESERVED");
            return new ResponseMessageDto("Reservation updated successfully", true, 200, null);
        } else {
            return new ResponseMessageDto("Error updating reservation", false, 500, null);
        }
    }

    public List<Reservations> getAllReservations() {
        return jdbcTemplate.query(SQL_GET_ALL_RESERVATIONS, new ReservationRowMapper());
    }

    public List<ParkingSpots> getAvailableSpots(String startTime, String endTime, int lotId) {
        return jdbcTemplate.query(SQL_GET_AVAILABLE_SPOTS, new Object[]{lotId, startTime, endTime}, new ParkingSpotRowMapper());
    }

    public List<Reservations> getUserReservations(int userId) {
        return jdbcTemplate.query(SQL_GET_USER_RESERVATIONS, new Object[]{userId}, new ReservationRowMapper());
    }

    public ResponseMessageDto expireReservations() {
        int rowsAffected = jdbcTemplate.update(SQL_MARK_EXPIRED_RESERVATIONS);
        return new ResponseMessageDto(
                rowsAffected > 0 ? "Expired reservations updated successfully" : "No reservations to expire",
                rowsAffected > 0,
                rowsAffected > 0 ? 200 : 204,
                null);
    }

    public int getTotalPenaltyForUser(int userId) {
        Integer totalPenalty = jdbcTemplate.queryForObject(SQL_USER_PENALTY, new Object[]{userId}, Integer.class);
        return totalPenalty != null ? totalPenalty : 0;
    }

    private boolean isSpotAlreadyBooked(int parkingSpotId, LocalDateTime startTime, LocalDateTime endTime) {
        String sql = "SELECT COUNT(*) FROM reservations WHERE parking_spot_id = ? " +
                "AND start_time < ? AND end_time > ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{parkingSpotId, endTime, startTime}, Integer.class);
        return count != null && count > 0;
    }

    private void createTimeSlotsForReservation(int parkingSpotId, LocalDateTime startTime, LocalDateTime endTime, String status) {
        String insertTimeSlot = "INSERT INTO time_slots (parking_spot_id, start_time, status) VALUES (?, ?, ?)";
        LocalDateTime currentTime = startTime;
        while (currentTime.isBefore(endTime) || currentTime.equals(endTime)) {
            jdbcTemplate.update(insertTimeSlot, parkingSpotId, currentTime, status);
            currentTime = currentTime.plusMinutes(30); // Assuming slots are 30 minutes, adjust as necessary
        }
    }

    private void updateTimeSlotsForReservation(int parkingSpotId, LocalDateTime startTime, LocalDateTime endTime, String status) {
        String updateTimeSlot = "UPDATE time_slots SET status = ? WHERE parking_spot_id = ? AND start_time BETWEEN ? AND ?";
        jdbcTemplate.update(updateTimeSlot, status, parkingSpotId, startTime, endTime);
    }

    private Reservations getReservationById(int id) {
        String sql = "SELECT id, penalty, start_time, end_time, duration, user_id, parking_spot_id, transaction_id FROM reservations WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new ReservationRowMapper());
    }

    public class ReservationRowMapper implements RowMapper<Reservations> {
        @Override
        public Reservations mapRow(ResultSet rs, int rowNum) throws SQLException {
            Reservations reservation = new Reservations();
            reservation.setId(rs.getInt("id"));
            reservation.setPenalty(rs.getInt("penalty"));
            reservation.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
            reservation.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
            reservation.setDuration(rs.getObject("duration", Duration.class));
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
}
