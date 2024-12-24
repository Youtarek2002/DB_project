package com.databaseproject.parkingproject.dao;

import com.databaseproject.parkingproject.dto.ResponseMessageDto;
import com.databaseproject.parkingproject.entity.Reservations;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ReservationDao {
    private static final String SQL_CHECK_SPOT_AVAILABILITY = "SELECT COUNT(*) FROM reservations WHERE parking_spot_id = ? AND start_time < ? AND end_time > ?;";
    private static final String SQL_INSERT_RESERVATION = "INSERT INTO reservations (parking_spot_id, user_id, start_time, end_time, penalty) VALUES (?, ?, ?, ?, ?);";
    private static final String SQL_DELETE_RESERVATION = "DELETE FROM reservations WHERE id = ?;";
    private static final String SQL_UPDATE_RESERVATION = "UPDATE reservations SET parking_spot_id = ?, user_id = ?, start_time = ?, end_time = ?, penalty = ? WHERE id = ?;";
    private static final String SQL_GET_ALL_RESERVATIONS = "SELECT * FROM reservations;";
    private static final String SQL_GET_AVAILABLE_SPOTS = "SELECT id FROM parking_spots WHERE id NOT IN (SELECT parking_spot_id FROM reservations WHERE start_time < ? AND end_time > ?);";
    private static final String SQL_GET_USER_RESERVATIONS = "SELECT * FROM reservations WHERE user_id = ?;";
    private static final String SQL_GET_PARKING_LOT_RESERVATIONS = "SELECT r.* FROM reservations r JOIN parking_spots ps ON r.parking_spot_id = ps.id WHERE ps.parking_lot_id = ?;";
    private static final String SQL_MARK_EXPIRED_RESERVATIONS = "UPDATE reservations SET penalty = penalty + 50 WHERE end_time < NOW() AND penalty = 0;";

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
        return jdbcTemplate.query(SQL_GET_ALL_RESERVATIONS, new BeanPropertyRowMapper<>(Reservations.class));
    }

    public List<Integer> getAvailableSpots(String startTime, String endTime) {
        return jdbcTemplate.queryForList(SQL_GET_AVAILABLE_SPOTS, new Object[]{endTime, startTime}, Integer.class);
    }

    public List<Reservations> getUserReservations(int userId) {
        return jdbcTemplate.query(SQL_GET_USER_RESERVATIONS, new Object[]{userId}, new BeanPropertyRowMapper<>(Reservations.class));
    }

    public List<Reservations> getParkingLotReservations(int parkingLotId) {
        return jdbcTemplate.query(SQL_GET_PARKING_LOT_RESERVATIONS, new Object[]{parkingLotId}, new BeanPropertyRowMapper<>(Reservations.class));
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

}
