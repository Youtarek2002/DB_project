package com.databaseproject.parkingproject.service;

import com.databaseproject.parkingproject.entity.ParkingLots;
import com.databaseproject.parkingproject.entity.ParkingSpots;
import com.databaseproject.parkingproject.entity.Users;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReportService {

    private final JdbcTemplate jdbcTemplate;
    private final PdfReportService pdfReportService;
    public void generateAdminReport() {

        String findTopUsers = "SELECT * FROM users ORDER BY number_of_reservations DESC LIMIT 5";
        List<Users> topUsers = jdbcTemplate.query(findTopUsers, new BeanPropertyRowMapper<>(Users.class));

        String findTopLots="SELECT * FROM parking_lots ORDER BY revenue DESC LIMIT 5";
        List<ParkingLots> topLots = jdbcTemplate.query(findTopLots, new BeanPropertyRowMapper<>(ParkingLots.class));

        pdfReportService.generateAdminReport(topUsers, topLots);
    }

    public void generateManagerReport(int managerId) {
        List<ParkingSpots> topSpotsByRevenue = jdbcTemplate.query("SELECT * FROM parking_spots WHERE parking_lot_id IN (SELECT id FROM parking_lots WHERE parking_lot_manager = ?) ORDER BY revenue DESC LIMIT 5",
                new BeanPropertyRowMapper<>(ParkingSpots.class), managerId);
        List<ParkingSpots> topSpotsByPenalty= jdbcTemplate.query("SELECT * FROM parking_spots WHERE parking_lot_id IN (SELECT id FROM parking_lots WHERE parking_lot_manager = ?) ORDER BY penalty DESC LIMIT 5",
                new BeanPropertyRowMapper<>(ParkingSpots.class), managerId);


        pdfReportService.generateManagerReport(topSpotsByRevenue, topSpotsByPenalty);
    }
}
