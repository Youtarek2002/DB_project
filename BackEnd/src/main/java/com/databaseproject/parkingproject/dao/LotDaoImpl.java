package com.databaseproject.parkingproject.dao;

import com.databaseproject.parkingproject.dto.ResponseMessageDto;
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
    private static final String SQL_INSERT_LOT = "INSERT INTO parking_lots (disabled_count, regular_count, EV_count, location, parking_lot_manager, admitted) VALUES (?, ?, ?, ?, ?, ?);";
    private static final String SQL_DELETE_LOT = "DELETE FROM parking_lots WHERE id = ?;";
    private static final String SQL_UPDATE_LOT_REGULAR_COUNT = "UPDATE parking_lots SET regular_count = ? WHERE id = ?;";
    private static final String SQL_UPDATE_LOT_DISABLED_COUNT = "UPDATE parking_lots SET disabled_count = ? WHERE id = ?;";
    private static final String SQL_UPDATE_LOT_EV_COUNT = "UPDATE parking_lots SET EV_count = ? WHERE id = ?;";
    private static final String SQL_ADDMIT_LOT =  "UPDATE parking_lots SET admitted = ? WHERE id = ?;";
    private static final String SQL_INSERT_SPOT ="INSERT INTO parking_spots (type ,parking_lot_id) VALUES (?,?);";
    private static final String SQL_GET_LOT_BY_ID ="SELECT * FROM parking_lots WHERE id = ?";
    private static final String SQL_GET_NEW_SPOT ="SELECT id FROM parking_spots \n" +
            "WHERE id NOT IN (SELECT DISTINCT parking_spot_id FROM time_slots)\n" +
            "LIMIT 1;";
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


    private static final String SQL_GET_PENDING_LOTS = "SELECT * FROM parking_lots WHERE admitted = false";
    private static final String SQL_GET_MANAGER_LOTS = "SELECT * FROM parking_lots WHERE parking_lot_manager = ?";

    private final JdbcTemplate jdbcTemplate;

    public LotDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ResponseMessageDto requsetInsertingLot(ParkingLots lot) {
        if (lot.getEVCount() == null || lot.getRegularCount() == null || lot.getDisabledCount() == null) {
            return new ResponseMessageDto("All counts must be provided", false, 400, null);
        }

        int k = jdbcTemplate.update(SQL_INSERT_LOT,
                lot.getDisabledCount(),
                lot.getRegularCount(),
                lot.getEVCount(),
                lot.getLocation(),
                lot.getManagerId(),
                false);

        String message = (k == 1) ? "Request has been sent" : "Error in sending the request";
        return new ResponseMessageDto(message, k == 1, k == 1 ? 200 : 404, null);
    }

    public ResponseMessageDto addmitLot(int id) {
        int k = jdbcTemplate.update(SQL_ADDMIT_LOT, true,id);

        String message = (k == 1) ? "Request has been sent" : "Error in sending the request";
        ParkingLots parkingLots = getParkingLot(id);
        System.out.println("TEESSST  "+parkingLots.toString());
        generateLotSpots(id,parkingLots.getDisabledCount(),"DISABLED" );
        generateLotSpots(id,parkingLots.getRegularCount(),"REGULAR" );
        generateLotSpots(id,parkingLots.getEVCount(),"EV");
        return new ResponseMessageDto(message, k == 1, k == 1 ? 200 : 404, null);
    }

    private ParkingLots getParkingLot(int id) {
        return jdbcTemplate.queryForObject(SQL_GET_LOT_BY_ID, new Object[]{id},
                (rs, rowNum) -> {
                    Integer disabledCount = rs.getObject("disabled_count", Integer.class);
                    Integer regularCount = rs.getObject("regular_count", Integer.class);
                    Integer EVCount = rs.getObject("EV_count", Integer.class);
                    String location = rs.getString("location");
                    Integer parkingLotManager = rs.getObject("parking_lot_manager", Integer.class);
                    Boolean admitted = rs.getObject("admitted", Boolean.class);

                    System.out.println("Mapped ParkingLot: " + disabledCount + ", " + regularCount + ", " + EVCount + ", " + location);

                    return new ParkingLots(
                            rs.getInt("id"),
                            disabledCount,
                            regularCount,
                            EVCount,
                            location,
                            parkingLotManager,
                            admitted
                    );

    });

    }

    private void  generateLotSpots(int lotId,int count , String type)
    {
          for(int i=0; i<count; i++)
          {
              jdbcTemplate.update(SQL_INSERT_SPOT,type,lotId);
              generateSpotTimeSlots();
          }
    }
    private void generateSpotTimeSlots()
    {
         jdbcTemplate.update(SQL_INSERT_TIME_SLOTS);
    }
    public ResponseMessageDto denyAddingLot(int id)
    {
        int k =jdbcTemplate.update(SQL_DELETE_LOT,new Object[]{id});
        return new ResponseMessageDto(k==1?"SUCCESS":"FAILURE", k == 1, k == 1 ? 200 : 404, null);
    }

    public List<ParkingLots> getAllPendingLots() {
        return jdbcTemplate.query(SQL_GET_PENDING_LOTS, new BeanPropertyRowMapper<>(ParkingLots.class));
    }


    public List<ParkingLots> getManagerLots(int managerId) {
        return  jdbcTemplate.query(SQL_GET_MANAGER_LOTS,new Object[]{managerId}, new ParkingLotsRowMapper());
    }
    public class ParkingLotsRowMapper implements RowMapper<ParkingLots> {
        @Override
        public ParkingLots mapRow(ResultSet rs, int rowNum) throws SQLException {
            ParkingLots parkingLot = new ParkingLots();
            parkingLot.setId(rs.getInt("id"));
            parkingLot.setDisabledCount(rs.getInt("disabled_count"));
            parkingLot.setRegularCount(rs.getInt("regular_count"));
            parkingLot.setEVCount(rs.getObject("ev_count", Integer.class)); // Handles nulls correctly
            parkingLot.setLocation(rs.getString("location"));
            parkingLot.setManagerId(rs.getInt("parking_lot_manager"));
            parkingLot.setAdmitted(rs.getBoolean("admitted"));
            return parkingLot;
        }
    }

}
