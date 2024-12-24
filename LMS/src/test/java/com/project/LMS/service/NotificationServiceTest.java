package com.project.LMS.service;

import com.project.LMS.dao.Notification.NotificationDao;
import com.project.LMS.dto.notifications.NotificationResponse;
import com.project.LMS.entity.InstructorNotification;
import com.project.LMS.entity.StudentNotification;
import com.project.LMS.entity.users.Instructor;
import com.project.LMS.entity.users.Student;
import com.project.LMS.repository.InstructorRepository;
import com.project.LMS.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private NotificationDao notificationDao;

    @Mock
    private EmailSenderService emailSenderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testSendNotificationToUser_Student() {
        Student student = new Student();
        student.setId(1);
        student.setEmail("student@example.com");

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));

        notificationService.sendNotificationToUser(1, "Test Message", "STUDENT");

        verify(notificationDao, times(1)).createMessageForUser(1, "Test Message", "STUDENT");
        verify(emailSenderService, times(1)).sendEmail("student@example.com", "LMS Notification", "Test Message");
    }
    @Test
    public void testSendNotificationToUser_Instructor() {
        Instructor instructor = new Instructor();
        instructor.setId(1);
        instructor.setEmail("instructor@example.com");

        when(instructorRepository.findById(1)).thenReturn(Optional.of(instructor));

        notificationService.sendNotificationToUser(1, "Test Message", "INSTRUCTOR");

        verify(notificationDao, times(1)).createMessageForUser(1, "Test Message", "INSTRUCTOR");
        verify(emailSenderService, times(1)).sendEmail("instructor@example.com", "LMS Notification", "Test Message");
    }
    @Test
    public void testGetUnReadNotificationsForUser() {
        Student student = new Student();
        student.setId(1);

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(notificationDao.getUnReadNotificationsForUser(1, "STUDENT")).thenReturn(List.of());

        List<NotificationResponse> notifications = notificationService.getUnReadNotificationsForUser(1, "STUDENT");

        assertEquals(0, notifications.size());
        verify(notificationDao, times(1)).getUnReadNotificationsForUser(1, "STUDENT");
    }
    @Test
    public void testGetAllNotificationsForUser() {
        Instructor instructor = new Instructor();
        instructor.setId(1);

        when(instructorRepository.findById(1)).thenReturn(Optional.of(instructor));
        when(notificationDao.getAllNotificationsForUser(1, "INSTRUCTOR")).thenReturn(List.of());

        List<NotificationResponse> notifications = notificationService.getAllNotificationsForUser(1, "INSTRUCTOR");

        assertEquals(0, notifications.size());
        verify(notificationDao, times(1)).getAllNotificationsForUser(1, "INSTRUCTOR");
    }
    @Test
    public void testMarkAsRead_Student() {
        Student student = new Student();
        student.setId(1);

        StudentNotification studentNotification = new StudentNotification();
        studentNotification.setId(100);

        when(studentRepository.findNotificationById(100)).thenReturn(Optional.of(studentNotification));
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));

        notificationService.markAsRead(1, 100, "STUDENT");

        verify(studentRepository, times(1)).findNotificationById(100);
        verify(studentRepository, times(1)).findById(1);
        verify(notificationDao, times(1)).markAsRead(1, 100, "STUDENT");
    }
    @Test
    public void testMarkAsRead_Instructor() {
        // Arrange
        Instructor instructor = new Instructor();
        instructor.setId(2);

        InstructorNotification instructorNotification = new InstructorNotification();
        instructorNotification.setId(200);

        when(instructorRepository.findNotificationById(200)).thenReturn(Optional.of(instructorNotification));
        when(instructorRepository.findById(2)).thenReturn(Optional.of(instructor));

        // Act
        notificationService.markAsRead(2, 200, "INSTRUCTOR");

        // Assert
        verify(instructorRepository, times(1)).findNotificationById(200);
        verify(instructorRepository, times(1)).findById(2);
        verify(notificationDao, times(1)).markAsRead(2, 200, "INSTRUCTOR");
    }
    @Test
    public void testMarkAsRead_NotificationNotFound() {
        when(studentRepository.findNotificationById(300)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                notificationService.markAsRead(1, 300, "STUDENT")
        );
        assertEquals("Student Notification with ID 300 not found.", exception.getMessage());

        verify(studentRepository, times(1)).findNotificationById(300);
        verifyNoInteractions(notificationDao);
    }
    @Test
    public void testMarkAsRead_StudentNotFound() {
        // Arrange
        StudentNotification studentNotification = new StudentNotification();
        studentNotification.setId(100);

        when(studentRepository.findNotificationById(100)).thenReturn(Optional.of(studentNotification));
        when(studentRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                notificationService.markAsRead(1, 100, "STUDENT")
        );
        assertEquals("Student with ID 1 not found.", exception.getMessage());

        verify(studentRepository, times(1)).findNotificationById(100);
        verify(studentRepository, times(1)).findById(1);
        verifyNoInteractions(notificationDao);
    }
}
