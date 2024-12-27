package com.databaseproject.parkingproject.service;

import com.databaseproject.parkingproject.dto.ResponseMessageDto;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NotificationService {
    private static final String SQL_admin_notification="SELECT time , message FROM notifications WHERE user_id=?";
    private static final String SQL_user_notification="SELECT time , message FROM notifications WHERE user_id=?";
    private static final String SQL_manager_notification="SELECT time , message FROM notifications WHERE user_id=?";
    private final JdbcTemplate jdbcTemplate;
    public ResponseMessageDto getAdminNotification(int adminId) {
        List<String> notifications = jdbcTemplate.query(
                SQL_admin_notification,
                new Object[]{adminId}, // Pass adminId as a parameter
                (rs, rowNum) -> rs.getString("time") + " : " + rs.getString("message") // Map rows to a list of notifications
        );

        if (notifications.isEmpty()) {
            // No notifications found
            return ResponseMessageDto.builder()
                    .message("No notifications found")
                    .success(true)
                    .statusCode(200)
                    .data(null)
                    .build();
        } else {
            // Notifications found
            return ResponseMessageDto.builder()
                    .message("success")
                    .success(true)
                    .statusCode(200)
                    .data(notifications) // Return the list of notifications
                    .build();
        }
    }

    public ResponseMessageDto getUserNotification(int userId) {
        List<String> notifications = jdbcTemplate.query(
                SQL_user_notification,
                new Object[]{userId}, // Pass userId as a parameter
                (rs, rowNum) -> rs.getString("time") + " : " + rs.getString("message") // Map rows to a list of notifications
        );

        if (notifications.isEmpty()) {
            // No notifications found
            return ResponseMessageDto.builder()
                    .message("No notifications found")
                    .success(true)
                    .statusCode(200)
                    .data(null)
                    .build();
        } else {
            // Notifications found
            return ResponseMessageDto.builder()
                    .message("success")
                    .success(true)
                    .statusCode(200)
                    .data(notifications) // Return the list of notifications
                    .build();
        }
    }

    public ResponseMessageDto getManagerNotification(int managerId) {
        List<String> notifications = jdbcTemplate.query(
                SQL_manager_notification,
                new Object[]{managerId}, // Pass managerId as a parameter
                (rs, rowNum) -> rs.getString("time") + " : " + rs.getString("message") // Map rows to a list of notifications
        );

        if (notifications.isEmpty()) {
            // No notifications found
            return ResponseMessageDto.builder()
                    .message("No notifications found")
                    .success(true)
                    .statusCode(200)
                    .data(null)
                    .build();
        } else {
            // Notifications found
            return ResponseMessageDto.builder()
                    .message("success")
                    .success(true)
                    .statusCode(200)
                    .data(notifications) // Return the list of notifications
                    .build();
        }
    }
}
