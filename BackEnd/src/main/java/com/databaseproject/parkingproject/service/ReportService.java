package com.databaseproject.parkingproject.service;

import com.databaseproject.parkingproject.entity.ParkingLots;
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

        pdfReportService.generatePDFReport(topUsers, topLots);
    }
}
