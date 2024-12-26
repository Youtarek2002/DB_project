package com.databaseproject.parkingproject.dao;

import com.databaseproject.parkingproject.dto.ResponseMessageDto;
import com.databaseproject.parkingproject.entity.ParkingLots;
import com.databaseproject.parkingproject.service.DynamicPriceService;
import com.databaseproject.parkingproject.entity.ParkingLots;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class LotDaoImpl {
    private static final String SQL_INSERT_LOT = "INSERT INTO parking_lots (disabled_count, regular_count, EV_count, location, latitude, longitude, parking_lot_manager, admitted) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String SQL_DELETE_LOT = "DELETE FROM parking_lots WHERE id = ?;";
    private static final String SQL_UPDATE_LOT_REGULAR_COUNT = "UPDATE parking_lots SET regular_count = ? WHERE id = ?;";
    private static final String SQL_UPDATE_LOT_DISABLED_COUNT = "UPDATE parking_lots SET disabled_count = ? WHERE id = ?;";
    private static final String SQL_UPDATE_LOT_EV_COUNT = "UPDATE parking_lots SET EV_count = ? WHERE id = ?;";
    private static final String SQL_ADMIT_LOT =  "UPDATE parking_lots SET admitted = ? WHERE id = ?;";
    private static final String SQL_INSERT_SPOT = "INSERT INTO parking_spots (type, parking_lot_id,status,price) VALUES (?, ?,?,?);";
    private static final String SQL_GET_LOT_BY_ID = "SELECT * FROM parking_lots WHERE id = ?;";
    private static final String SQL_GET_NEW_SPOT = "SELECT id FROM parking_spots WHERE id NOT IN (SELECT DISTINCT parking_spot_id FROM time_slots) LIMIT 1;";
    private static final String SQL_INSERT_TIME_SLOTS = """
    INSERT INTO time_slots (status, parking_spot_id, start_time)
    SELECT
        'AVAILABLE',
        (SELECT id FROM parking_spots WHERE id NOT IN (SELECT DISTINCT parking_spot_id FROM time_slots) LIMIT 1),
        ADDTIME('00:00:00', CONCAT(n.n, ':00:00'))
    FROM (SELECT 0 AS n UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION 
          SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION 
          SELECT 12 UNION SELECT 13 UNION SELECT 14 UNION SELECT 15 UNION SELECT 16 UNION SELECT 17 UNION 
          SELECT 18 UNION SELECT 19 UNION SELECT 20 UNION SELECT 21 UNION SELECT 22 UNION SELECT 23) n;
""";
    private static final String SQL_GET_PENDING_LOTS = "SELECT * FROM parking_lots WHERE admitted = false;";
    private static final String SQL_GET_MANAGER_LOTS = "SELECT * FROM parking_lots WHERE parking_lot_manager = ?;";
    private static final String SQL_FIND_NEAREST_LOTS = """
        SELECT *,
               (6371 * acos(cos(radians(?)) * cos(radians(latitude))
               * cos(radians(longitude) - radians(?)) + sin(radians(?))
               * sin(radians(latitude)))) AS distance
        FROM parking_lots
        WHERE admitted = true 
          AND (latitude != 0.0 AND longitude != 0.0)  -- Exclude invalid coordinates
        ORDER BY distance ASC
        LIMIT ?;
    """;

    private final JdbcTemplate jdbcTemplate;
    private final DynamicPriceService dynamicPriceService;

    public LotDaoImpl(JdbcTemplate jdbcTemplate, DynamicPriceService dynamicPriceService) {
        this.jdbcTemplate = jdbcTemplate;
        this.dynamicPriceService = dynamicPriceService;
    }

    public ResponseMessageDto requsetInsertingLot(ParkingLots lot) {
        if (lot.getEVCount() == null || lot.getRegularCount() == null || lot.getDisabledCount() == null) {
            return new ResponseMessageDto("All counts must be provided", false, 400, null);
        }

        int rowsAffected = jdbcTemplate.update(SQL_INSERT_LOT,
                lot.getDisabledCount(),
                lot.getRegularCount(),
                lot.getEVCount(),
                lot.getLocation(),
                lot.getLatitude(),
                lot.getLongitude(),
                lot.getManagerId(),
                false);

        String message = (rowsAffected == 1) ? "Request has been sent" : "Error in sending the request";
        return new ResponseMessageDto(message, rowsAffected == 1, rowsAffected == 1 ? 200 : 404, null);
    }

    public ResponseMessageDto admitLot(int id) {
        int rowsAffected = jdbcTemplate.update(SQL_ADMIT_LOT, true, id);

        String message = (rowsAffected == 1) ? "Request has been sent" : "Error in sending the request";
        ParkingLots parkingLot = getParkingLot(id);
        int price=dynamicPriceService.calculatePrice(id);

        generateLotSpots(id, parkingLot.getDisabledCount(), "DISABLED",price);
        generateLotSpots(id, parkingLot.getRegularCount(), "REGULAR",price);
        generateLotSpots(id, parkingLot.getEVCount(), "EV",price);

        return new ResponseMessageDto(message, rowsAffected == 1, rowsAffected == 1 ? 200 : 404, null);
    }

    private ParkingLots getParkingLot(int id) {
        return jdbcTemplate.queryForObject(SQL_GET_LOT_BY_ID, new Object[]{id},
                (rs, rowNum) -> new ParkingLots(
                        rs.getInt("id"),
                        rs.getInt("disabled_count"),
                        rs.getInt("regular_count"),
                        rs.getObject("EV_count", Integer.class),
                        rs.getString("location"),
                        rs.getDouble("latitude"),
                        rs.getInt("revenue"),
                        rs.getDouble("longitude"),
                        rs.getInt("parking_lot_manager"),
                        rs.getBoolean("admitted")
                ));
    }

    private void generateLotSpots(int lotId, int count, String type,int price) {
        for (int i = 0; i < count; i++) {
            jdbcTemplate.update(SQL_INSERT_SPOT, type, lotId, "AVAILABLE",price);
            generateSpotTimeSlots();
        }
    }

    private void generateSpotTimeSlots() {
        jdbcTemplate.update(SQL_INSERT_TIME_SLOTS);
    }

    public ResponseMessageDto denyAddingLot(int id) {
        int rowsAffected = jdbcTemplate.update(SQL_DELETE_LOT, id);
        return new ResponseMessageDto(rowsAffected == 1 ? "SUCCESS" : "FAILURE", rowsAffected == 1, rowsAffected == 1 ? 200 : 404, null);
    }
    public ResponseMessageDto deleteLot(int id) {
        int rowsAffected = jdbcTemplate.update(SQL_DELETE_LOT, id);
        return new ResponseMessageDto(rowsAffected == 1 ? "SUCCESS" : "FAILURE", rowsAffected == 1, rowsAffected == 1 ? 200 : 404, null);
    }

    public List<ParkingLots> getAllPendingLots() {
        return jdbcTemplate.query(SQL_GET_PENDING_LOTS, new BeanPropertyRowMapper<>(ParkingLots.class));
    }


    public List<ParkingLots> getManagerLots(int managerId) {
        return jdbcTemplate.query(SQL_GET_MANAGER_LOTS, new Object[]{managerId}, new ParkingLotsRowMapper());
    }
    public List<ParkingLots> findNearestParkingLots(double userLatitude, double userLongitude, int limit) {
        return jdbcTemplate.query(SQL_FIND_NEAREST_LOTS, new Object[]{userLatitude, userLongitude, userLatitude, limit},new ParkingLotsRowMapper());
    }




    public static class ParkingLotsRowMapper implements RowMapper<ParkingLots> {
        @Override
        public ParkingLots mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ParkingLots(
                    rs.getInt("id"),
                    rs.getInt("disabled_count"),
                    rs.getInt("regular_count"),
                    rs.getObject("EV_count", Integer.class),
                    rs.getString("location"),
                    rs.getDouble("latitude"),
                    rs.getInt("revenue"),
                    rs.getDouble("longitude"),
                    rs.getInt("parking_lot_manager"),
                    rs.getBoolean("admitted")
            );
        }
    }

}
