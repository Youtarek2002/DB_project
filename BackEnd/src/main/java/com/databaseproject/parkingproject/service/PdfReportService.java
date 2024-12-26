package com.databaseproject.parkingproject.service;

import com.databaseproject.parkingproject.entity.ParkingLots;
import com.databaseproject.parkingproject.entity.Users;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.kernel.pdf.PdfDocument;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PdfReportService {

    public void generatePDFReport(List<Users> topUsers, List<ParkingLots> topLots) {
        // Define the output PDF file
        String outputPath = "report.pdf";

        try {
            // Create a PdfWriter instance for the output file
            PdfWriter writer = new PdfWriter(outputPath);

            // Create a PdfDocument instance with the writer
            PdfDocument pdfDocument = new PdfDocument(writer);

            // Create a Document instance to add content
            Document document = new Document(pdfDocument);

            // Add content for Top Users with Table
            document.add(new Paragraph("Top Users by Reservations"));
            Table userTable = new Table(2); // 2 columns: Rank and User Info
            userTable.addHeaderCell("Rank");
            userTable.addHeaderCell("User Name - Number of Reservations");

            int i = 1;
            for (Users user : topUsers) {
                userTable.addCell(String.valueOf(i));  // Rank
                userTable.addCell(user.getFname() + ": " + user.getNumberOfReservations());  // User info
                i++;
            }
            document.add(userTable);

            // Add content for Top Parking Lots with Table
            document.add(new Paragraph("Top Parking Lots by Revenue"));
            Table lotTable = new Table(2); // 2 columns: Rank and Lot Info
            lotTable.addHeaderCell("Rank");
            lotTable.addHeaderCell("Parking Lot Name - Revenue");

            i = 1;
            for (ParkingLots lot : topLots) {
                lotTable.addCell(String.valueOf(i));  // Rank
                lotTable.addCell(lot.getLocation() + ": " + lot.getRevenue());  // Parking lot info
                i++;
            }
            document.add(lotTable);

            // Close the document
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
