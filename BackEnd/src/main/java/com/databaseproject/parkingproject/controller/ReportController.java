package com.databaseproject.parkingproject.controller;

import com.databaseproject.parkingproject.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/generate-admin-report")
    public ResponseEntity<byte[]> generateAdminReport() {
        // Generate the PDF report
        reportService.generateAdminReport();

        // Define the path of the generated PDF
        String pdfFilePath = "reportAdmin.pdf";
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
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=reportAdmin.pdf");

            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/generate-manager-report")
    public ResponseEntity<byte[]> generateManagerReport(@RequestParam int managerId) {
        // Generate the PDF report
        System.out.println("managerId = " + managerId);
        reportService.generateManagerReport(managerId);

        // Define the path of the generated PDF
        String pdfFilePath = "reportManager.pdf";
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
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=reportManager.pdf");

            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
