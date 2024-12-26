package com.databaseproject.parkingproject.controller;

import com.databaseproject.parkingproject.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/api/authenticate/report")
public class ReportController {

    private final ReportService reportService;
    @GetMapping("/generate-report")
    public ResponseEntity<byte[]> generateReport() {
        // Generate the PDF report
        reportService.generateAdminReport();

        // Define the path of the generated PDF
        String pdfFilePath = "report.pdf";
        File pdfFile = new File(pdfFilePath);

        // Check if the file exists
        if (!pdfFile.exists()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // Read the file into a byte array
        try (InputStream in = new FileInputStream(pdfFile)) {
            byte[] pdfContent = in.readAllBytes();

            // Set headers and return the PDF content
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=report.pdf");

            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
