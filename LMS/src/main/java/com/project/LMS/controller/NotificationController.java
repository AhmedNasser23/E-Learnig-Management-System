package com.project.LMS.controller;

import com.project.LMS.dto.notifications.NotificationBody;
import com.project.LMS.dto.notifications.NotificationResponse;
import com.project.LMS.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    public String sendNotificationToStudent(@RequestBody NotificationBody notificationBody) {
        try {
            int userId = notificationBody.getUserId();
            String message = notificationBody.getMessage();
            String role = notificationBody.getRole();

            notificationService.sendNotificationToUser(userId, message, role);

            switch (notificationBody.getRole()) {
                case "STUDENT" -> {
                    return "Notification sent successfully to Student with ID: " + userId;
                }
                case "INSTRUCTOR" -> {
                    return "Notification sent successfully to Instructor with ID: " + userId;
                }
            }
            return "";
        } catch (Exception e) {
            return "Error sending notification: " + e.getMessage();
        }
    }

    @GetMapping("/unread")
    public List<NotificationResponse> getUnreadNotifications(@RequestBody NotificationBody notificationBody) {
        try {
            return notificationService.getUnReadNotificationsForUser(notificationBody.getUserId(), notificationBody.getRole());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching unread notifications: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public List<NotificationResponse> getAllNotifications(@RequestBody NotificationBody notificationBody) {
        try {
            return notificationService.getAllNotificationsForUser(notificationBody.getUserId(), notificationBody.getRole());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching all notifications: " + e.getMessage());
        }
    }

    @PutMapping("/markAsRead")
    public String markAsRead(@RequestParam int userId, @RequestParam int notificationId, @RequestParam String role) {
        try {
            notificationService.markAsRead(userId, notificationId, role);
            switch (role) {
                case "STUDENT" -> {
                    return "Student marked as read successfully";
                }
                case "INSTRUCTOR" -> {
                    return "Instructor marked as read successfully";
                }
            }
            return "";
        } catch (Exception e) {
            return "Error marking notification as read: " + e.getMessage();
        }
    }
}
