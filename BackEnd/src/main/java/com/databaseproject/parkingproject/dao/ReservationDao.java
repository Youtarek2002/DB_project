package com.databaseproject.parkingproject.dao;

import java.io.Serializable;

import com.databaseproject.parkingproject.dto.ReservationDto;
import com.databaseproject.parkingproject.dto.ResponseMessageDto;
import com.databaseproject.parkingproject.entity.ParkingSpots;
import com.databaseproject.parkingproject.entity.Reservations;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.transaction.annotation.Isolation;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ReservationDao {
    private static final String SQL_GET_RESERVATION_BY_ID = "SELECT * FROM reservations WHERE id = ?";
//    private static final String SQL_UPDATE_TIME_SLOT = "UPDATE time_slots SET status = ? WHERE parking_spot_id = ? AND start_time BETWEEN ? AND ?";
//    private static final String SQL_INSERT_TIME_SLOT = "INSERT INTO time_slots (parking_spot_id, start_time, end_time, status) VALUES (?, ?, ?, ?)";
    private static final String SQL_INSERT_RESERVATION = "INSERT INTO reservations (parking_spot_id, user_id, start_time, end_time, penalty , cost) VALUES (?, ?, ?, ?, ?,?);";
    private static final String SQL_DELETE_RESERVATION = "DELETE FROM reservations WHERE id = ?;";
    private static final String SQL_UPDATE_RESERVATION = "UPDATE reservations SET parking_spot_id = ?, user_id = ?, start_time = ?, end_time = ?, penalty = ? WHERE id = ?;";
    private static final String SQL_GET_ALL_RESERVATIONS = "SELECT id, penalty, start_time, end_time, duration, user_id, parking_spot_id, transaction_id FROM reservations;";
    private static final String SQL_GET_USER_RESERVATIONS =
            "SELECT pl.location, pl.longitude, pl.latitude , r.*" +
                    "FROM reservations r " +
                    "JOIN parking_spots ps ON r.parking_spot_id = ps.id " +
                    "JOIN parking_lots pl ON ps.parking_lot_id = pl.id " +
                    "WHERE r.user_id = ?";
    private static final String SQL_GET_VALID_RESERVATIONS = "SELECT * FROM reservations WHERE user_id = ? AND penalty =0;";

    private static final String SQL_MARK_EXPIRED_RESERVATIONS = """
    UPDATE reservations r
    JOIN parking_spots ps ON r.parking_spot_id = ps.id
    SET r.penalty = r.cost / 2
    WHERE r.start_time < NOW() - INTERVAL 15 MINUTE
      AND r.penalty = 0
      AND ps.status = 'RESERVED';
""";
    private static final String SQL_UPDATE_PARKING_SPOTS_PENALTY =
            "UPDATE parking_spots ps " +
                    "JOIN reservations r ON ps.id = r.parking_spot_id " +
                    "SET ps.penalty = r.cost / 2 " +
                    "WHERE r.start_time < NOW() - INTERVAL 15 MINUTE "+
                    "AND ps.status = 'RESERVED' ";
    private static final String SQL_GET_AVAILABLE_SPOTS = "SELECT ps.id, ps.type, ps.price, ps.parking_lot_id, ts.status " +
            "FROM parking_spots ps " +
            "LEFT JOIN time_slots ts ON ps.id = ts.parking_spot_id " +
            "WHERE ps.parking_lot_id = ? " +
            "AND (ts.start_time IS NULL OR ts.start_time NOT BETWEEN ? AND ? OR ts.status = 'AVAILABLE')";
    private static final String SQL_USER_PENALTY = "SELECT SUM(penalty) FROM reservations WHERE user_id = ?";
    private static final String SQL_GET_PARKING_LOT_RESERVATIONS = "SELECT r.* FROM reservations r JOIN parking_spots ps ON r.parking_spot_id = ps.id WHERE ps.parking_lot_id = ?;";
    private static final String SQL_CHECK_SPOT_AVAILABILITY = "SELECT COUNT(*) FROM reservations WHERE parking_spot_id = ? AND start_time < ? AND end_time > ? AND penalty=0 FOR UPDATE";
    private static final String SQL_GET_EXPIRED_USER_RESERVATIONS =
            "SELECT * FROM reservations WHERE user_id = ? AND end_time < NOW() AND penalty>0";

    private static final String SQL_GET_EXPIRED_LOT_RESERVATIONS =
            "SELECT r.* " +
                    "FROM reservations r " +
                    "JOIN parking_spots ps ON r.parking_spot_id = ps.id " +
                    "WHERE ps.parking_lot_id = ? AND r.end_time < NOW() AND penalty>0";
    private static final String SQL_GET_RESERVATIONS_BY_TIME =
            "SELECT * FROM reservations WHERE start_time <= ? AND end_time > ? AND penalty = 0";
    private static final String SQL_user_notification="Insert into notifications (user_id,time , message) values (?,?,?)";

    private final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseMessageDto createReservation(Reservations reservation) {
        if (isSpotAlreadyBooked(reservation.getParkingSpotId(), reservation.getStartTime(), reservation.getEndTime())) {
            return new ResponseMessageDto("Parking spot already reserved for the selected time.", false, 400, null);
        }
        jdbcTemplate.update(SQL_user_notification,reservation.getUserId(),LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),"Your reservation for spot number " + reservation.getParkingSpotId()+ " is confirmed");
        int rowsAffected = jdbcTemplate.update(SQL_INSERT_RESERVATION,
                reservation.getParkingSpotId(),
                reservation.getUserId(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getPenalty(),
                reservation.getCost());
          //  createTimeSlotsForReservation(reservation.getParkingSpotId(), reservation.getStartTime(), reservation.getEndTime(), "RESERVED");
            return new ResponseMessageDto("Reservation created successfully", true, 200, null);
    }

    public ResponseMessageDto deleteReservation(int id) {
        Reservations reservation = getReservationById(id);
        if (reservation != null) {
            int rowsAffected = jdbcTemplate.update(SQL_DELETE_RESERVATION, id);

            if (rowsAffected == 1) {
           //     updateTimeSlotsForReservation(reservation.getParkingSpotId(), reservation.getStartTime(), reservation.getEndTime(), "AVAILABLE");
                return new ResponseMessageDto("Reservation deleted successfully", true, 200, null);
            }
        }
        return new ResponseMessageDto("Error deleting reservation", false, 500, null);
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

        if (rowsAffected == 1) {
        //    updateTimeSlotsForReservation(reservation.getParkingSpotId(), reservation.getStartTime(), reservation.getEndTime(), "RESERVED");
            return new ResponseMessageDto("Reservation updated successfully", true, 200, null);
        } else {
            return new ResponseMessageDto("Error updating reservation", false, 500, null);
        }
    }
    public List<Reservations> getParkingLotReservations(int parkingLotId) {
        return jdbcTemplate.query(SQL_GET_PARKING_LOT_RESERVATIONS, new Object[]{parkingLotId}, new ReservationRowMapper());
    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    private boolean isSpotAlreadyBooked(int parkingSpotId, LocalDateTime startTime,LocalDateTime  endTime) {
        Integer count = jdbcTemplate.queryForObject(SQL_CHECK_SPOT_AVAILABILITY, new Object[]{parkingSpotId, endTime, startTime}, Integer.class);
        return count != null && count > 0;
    }

    public List<Reservations> getAllReservations() {
        return jdbcTemplate.query(SQL_GET_ALL_RESERVATIONS, new ReservationRowMapper());
    }

    public Map<String, Object> getAvailableSpots(String startTime, String endTime, int lotId) {
        List<ParkingSpots> parkingSpots = jdbcTemplate.query(SQL_GET_AVAILABLE_SPOTS, new Object[]{lotId, startTime, endTime}, new ParkingSpotRowMapper());
        final LocalTime peakStart = LocalTime.of(14, 0);
        final LocalTime peakEnd = LocalTime.of(19, 0);
        final LocalTime currentTime = LocalTime.now();
        final LocalTime s = LocalTime.of(0, 0);
        final LocalTime e = LocalTime.of(8, 0);
        int price = 0;

        if (currentTime.isAfter(peakStart) && currentTime.isBefore(peakEnd)) {
            price += 20;
        } else if (!(currentTime.isAfter(s) && currentTime.isBefore(e))) {
            price += 10;
        }

        for (ParkingSpots p : parkingSpots) {
            p.setPrice(p.getPrice() + price);
        }

        Integer startIndex = jdbcTemplate.queryForObject(
                "SELECT MIN(id) FROM parking_spots WHERE parking_lot_id = ?",
                new Object[]{lotId}, Integer.class);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("spots", parkingSpots);
        resultMap.put("startIndex", startIndex != null ? startIndex : 0);

        return resultMap;
    }

    public List<ReservationDto> getUserReservations(int userId) {
        return jdbcTemplate.query(SQL_GET_USER_RESERVATIONS, new Object[]{userId}, (rs, rowNum) -> {
            ReservationDto reservation = new ReservationDto();
            reservation.setLocation(rs.getString("location"));
            reservation.setLongitude(rs.getDouble("longitude"));
            reservation.setLatitude(rs.getDouble("latitude"));
            reservation.setReservation(new ReservationRowMapper().mapRow(rs, rowNum));
            return reservation;
        });
}

    public ResponseMessageDto expireReservations() {
        int rowsAffected = jdbcTemplate.update(SQL_MARK_EXPIRED_RESERVATIONS);
        int k =jdbcTemplate.update(SQL_UPDATE_PARKING_SPOTS_PENALTY);
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
    public List<Reservations> getExpiredReservationsForUser(int userId) {
        return jdbcTemplate.query(SQL_GET_EXPIRED_USER_RESERVATIONS, new Object[]{userId}, new ReservationRowMapper());
    }

    public List<Reservations> getExpiredReservationsForLot(int lotId) {
        return jdbcTemplate.query(SQL_GET_EXPIRED_LOT_RESERVATIONS, new Object[]{lotId}, new ReservationRowMapper());
    }


//    private void createTimeSlotsForReservation(int parkingSpotId, LocalDateTime startTime, LocalDateTime endTime, String status) {
//        LocalDateTime currentTime = startTime;
//        while (currentTime.isBefore(endTime) || currentTime.equals(endTime)) {
//            LocalDateTime nextTime = currentTime.plusMinutes(60);
//            jdbcTemplate.update(SQL_INSERT_TIME_SLOT, parkingSpotId, currentTime, nextTime, status);
//            currentTime = nextTime;
//        }
//    }
//
//    private void updateTimeSlotsForReservation(int parkingSpotId, LocalDateTime startTime, LocalDateTime endTime, String status) {
//        jdbcTemplate.update(SQL_UPDATE_TIME_SLOT, status, parkingSpotId, startTime, endTime);
//    }

    private Reservations getReservationById(int id) {
        return jdbcTemplate.queryForObject(SQL_GET_RESERVATION_BY_ID, new Object[]{id}, new ReservationRowMapper());
    }
    public List<Reservations> getReservationsByTime(LocalDateTime now) {
        return jdbcTemplate.query(
                SQL_GET_RESERVATIONS_BY_TIME,
                new Object[]{now, now},
                new ReservationRowMapper()
        );
    }

    public List<Reservations> getValidReservations(int userId) {
        return jdbcTemplate.query(SQL_GET_VALID_RESERVATIONS, new Object[]{userId}, new ReservationRowMapper());

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
            reservation.setCost(rs.getInt("cost"));
            return reservation;
        }
    }

    public class ParkingSpotRowMapper implements RowMapper<ParkingSpots> {
        @Override
        public ParkingSpots mapRow(ResultSet rs, int rowNum) throws SQLException {
            ParkingSpots spot = new ParkingSpots();
            spot.setId(rs.getInt("id"));

            String statusStr = rs.getString("status");
            if (statusStr != null) {
                try {
                    spot.setStatus(ParkingSpots.Status.valueOf(statusStr));
                } catch (IllegalArgumentException e) {
                    spot.setStatus(ParkingSpots.Status.AVAILABLE);
                    System.out.println("Invalid status value: " + statusStr);

                }
            } else {
                spot.setStatus(ParkingSpots.Status.AVAILABLE);
            }

            spot.setType(rs.getString("type"));
            spot.setPrice(rs.getInt("price"));
            spot.setParkingLotId(rs.getInt("parking_lot_id"));
            return spot;
        }
    }



}
