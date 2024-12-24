package com.project.LMS.service;

import com.project.LMS.dao.Notification.NotificationDao;
import com.project.LMS.dto.notifications.NotificationResponse;
import com.project.LMS.entity.users.Instructor;
import com.project.LMS.entity.users.Student;
import com.project.LMS.repository.InstructorRepository;
import com.project.LMS.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private NotificationDao notificationDao;

    @Autowired
    private EmailSenderService senderService;

    public void sendNotificationToUser(int userId, String message, String role) {

        Student student;
        Instructor instructor;
        String email = "";

        switch (role) {
            case "STUDENT":
                student = studentRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("Student with ID " + userId + " not found."));
                email = student.getEmail();
                break;

            case "INSTRUCTOR":
                instructor = instructorRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Instructor with ID " + userId + " not found."));
                email = instructor.getEmail();
                break;
        }

        notificationDao.createMessageForUser(userId, message, role);

        senderService.sendEmail(email, "LMS Notification", message);
    }
    public List<NotificationResponse> getUnReadNotificationsForUser(int userId, String role) {

        switch (role) {
            case "STUDENT":
                studentRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("Student with ID " + userId + " not found."));
                break;
            case "INSTRUCTOR":
                instructorRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("Instructor with ID " + userId + " not found."));
                break;
        }

        return notificationDao.getUnReadNotificationsForUser(userId, role);
    }
    public List<NotificationResponse> getAllNotificationsForUser(int userId, String role) {

        switch (role) {
            case "STUDENT":
                studentRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("Student with ID " + userId + " not found."));
                break;
            case "INSTRUCTOR":
                instructorRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("Instructor with ID " + userId + " not found."));
                break;
        }

        return notificationDao.getAllNotificationsForUser(userId, role);
    }
    public void markAsRead(int userId, int notificationId, String role) {
        switch (role) {
            case "STUDENT":
                studentRepository.findNotificationById(notificationId)
                        .orElseThrow(() -> new RuntimeException("Student Notification with ID " + notificationId + " not found."));
                studentRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("Student with ID " + userId + " not found."));
                break;

            case "INSTRUCTOR":
                instructorRepository.findNotificationById(notificationId)
                        .orElseThrow(() -> new RuntimeException("Instructor Notification with ID " + notificationId + " not found."));
                instructorRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("Instructor with ID " + userId + " not found."));
                break;

            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }

        notificationDao.markAsRead(userId, notificationId, role);
    }
}
