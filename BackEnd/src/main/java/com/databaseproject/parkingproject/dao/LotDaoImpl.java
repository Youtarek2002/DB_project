package com.databaseproject.parkingproject.dao;

import com.databaseproject.parkingproject.dto.ResponseMessageDto;
import com.databaseproject.parkingproject.entity.ParkingLots;
import com.databaseproject.parkingproject.service.DynamicPriceService;
import com.databaseproject.parkingproject.entity.ParkingLots;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class LotDaoImpl {
    private static final String SQL_INSERT_LOT = "INSERT INTO parking_lots (disabled_count, regular_count, EV_count,disabled_price,regular_price,EV_price, location, latitude, longitude, parking_lot_manager, admitted) VALUES (?, ?, ?, ?, ?, ? ,? ,? ,? ,? , false);";
    private static final String SQL_DELETE_LOT = "DELETE FROM parking_lots WHERE id = ?;";
    private static final String SQL_ADMIT_LOT =  "UPDATE parking_lots SET admitted = true WHERE id = ?;";
    private static final String SQL_INSERT_SPOT = "INSERT INTO parking_spots (type, parking_lot_id,status,price) VALUES (?, ?,?,?);";
    private static final String SQL_GET_LOT_BY_ID = "SELECT * FROM parking_lots WHERE id = ?;";
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
    final static String dropProcedure = "DROP PROCEDURE IF EXISTS GenerateParkingSpots;";
    final static String createProcedure = """
    CREATE PROCEDURE GenerateParkingSpots(
        IN lotId INT,
        IN disabledCount INT,
        IN regularCount INT,
        IN evCount INT,
        IN disabledPrice INT,
        IN regularPrice INT,
        IN evPrice INT
    )
    BEGIN
        DECLARE i INT DEFAULT 0;

        -- Insert DISABLED spots
        WHILE i < disabledCount DO
            INSERT INTO parking_spots (type, parking_lot_id, status, price)
            VALUES ('DISABLED', lotId, 'AVAILABLE', disabledPrice);
            SET i = i + 1;
        END WHILE;

        SET i = 0; -- Reset counter

        -- Insert REGULAR spots
        WHILE i < regularCount DO
            INSERT INTO parking_spots (type, parking_lot_id, status, price)
            VALUES ('REGULAR', lotId, 'AVAILABLE', regularPrice);
            SET i = i + 1;
        END WHILE;

        SET i = 0; -- Reset counter

        -- Insert EV spots
        WHILE i < evCount DO
            INSERT INTO parking_spots (type, parking_lot_id, status, price)
            VALUES ('EV', lotId, 'AVAILABLE', evPrice);
            SET i = i + 1;
        END WHILE;
    END;
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
                lot.getDisabledPrice(),
                lot.getRegularPrice(),
                lot.getEVPrice(),
                lot.getLocation(),
                lot.getLatitude(),
                lot.getLongitude(),
                lot.getManagerId()
        );

        String message = (rowsAffected == 1) ? "Request has been sent" : "Error in sending the request";
        return new ResponseMessageDto(message, rowsAffected == 1, rowsAffected == 1 ? 200 : 404, null);
    }

//    public ResponseMessageDto admitLot(int id) {
//        int rowsAffected = jdbcTemplate.update(SQL_ADMIT_LOT, id);
//
//        String message = (rowsAffected == 1) ? "Request has been sent" : "Error in sending the request";
//        ParkingLots parkingLot = getParkingLot(id);
//
//        generateLotSpots(id, parkingLot.getDisabledCount(), "DISABLED",parkingLot.getDisabledPrice());
//        generateLotSpots(id, parkingLot.getRegularCount(), "REGULAR",parkingLot.getRegularCount());
//        generateLotSpots(id, parkingLot.getEVCount(), "EV", parkingLot.getEVPrice());
//
//        return new ResponseMessageDto(message, rowsAffected == 1, rowsAffected == 1 ? 200 : 404, null);
//    }
public ResponseMessageDto admitLot(int id) {
    int rowsAffected = jdbcTemplate.update(SQL_ADMIT_LOT, id);

    String message = (rowsAffected == 1) ? "Request has been sent" : "Error in sending the request";
    ParkingLots parkingLot = getParkingLot(id);
    jdbcTemplate.execute(dropProcedure);
    jdbcTemplate.execute(createProcedure);
    jdbcTemplate.execute(
            "{CALL GenerateParkingSpots(?, ?, ?, ?, ?, ?, ?)}",
            (CallableStatementCallback<Object>) cs -> {
                cs.setInt(1, id);
                cs.setInt(2, parkingLot.getDisabledCount());
                cs.setInt(3, parkingLot.getRegularCount());
                cs.setInt(4, parkingLot.getEVCount());
                cs.setInt(5, parkingLot.getDisabledPrice());
                cs.setInt(6, parkingLot.getRegularPrice());
                cs.setInt(7, parkingLot.getEVPrice());
                cs.execute();
                return null;
            }
    );

    return new ResponseMessageDto(message, rowsAffected == 1, rowsAffected == 1 ? 200 : 404, null);
}


    private ParkingLots getParkingLot(int id) {
        return jdbcTemplate.queryForObject(SQL_GET_LOT_BY_ID, new Object[]{id}, new ParkingLotsRowMapper());
    }

    private void generateLotSpots(int lotId, int count, String type,int price) {
        for (int i = 0; i < count; i++) {
            jdbcTemplate.update(SQL_INSERT_SPOT, type, lotId, "AVAILABLE",price);
        }
    }

//    private void generateSpotTimeSlots() {
//        jdbcTemplate.update(SQL_INSERT_TIME_SLOTS);
//    }

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
                    rs.getInt("disabled_price"),
                    rs.getInt("regular_price"),
                    rs.getInt("EV_price"),
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
