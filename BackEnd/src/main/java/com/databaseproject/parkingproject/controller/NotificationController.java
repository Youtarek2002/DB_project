package com.databaseproject.parkingproject.controller;

import com.databaseproject.parkingproject.dto.ResponseMessageDto;
import com.databaseproject.parkingproject.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/api/authenticate/notification")
public class NotificationController {
    private final NotificationService notificationService;
    @GetMapping("/adminNotification")
    public ResponseMessageDto getAdminNotification() {
        return notificationService.getAdminNotification(1);
    }
    @GetMapping("/userNotification")
    public ResponseMessageDto getUserNotification(@RequestParam int userId) {
        return notificationService.getUserNotification(userId);
    }
    @GetMapping("/managerNotification")
    public ResponseMessageDto getManagerNotification(@RequestParam int managerId) {
        return notificationService.getManagerNotification(managerId);
    }

}
