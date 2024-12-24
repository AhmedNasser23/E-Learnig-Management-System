package com.project.LMS.dao.Notification;


import com.project.LMS.dto.notifications.NotificationResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class NotificationDaoTest {

    @InjectMocks
    private NotificationDao notificationDao;

    @Mock
    private JdbcTemplate jdbcTemplate;

    public NotificationDaoTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateMessageForUser_Student() {
        when(jdbcTemplate.update(anyString(), anyInt(), anyString())).thenReturn(1);
        notificationDao.createMessageForUser(1, "Test Message", "STUDENT");
        verify(jdbcTemplate, times(1)).update(anyString(), anyInt(), anyString());
    }


    @Test
    public void testCreateMessageForUser_Instructor() {
        when(jdbcTemplate.update(anyString(), anyInt(), anyString())).thenReturn(1);
        notificationDao.createMessageForUser(1, "Test Message", "INSTRUCTOR");
        verify(jdbcTemplate, times(1)).update(anyString(), anyInt(), anyString());
    }

    @Test
    public void testGetUnReadNotificationsForUser_Student() {
        when(jdbcTemplate.query(anyString(), any(Object[].class), (RowMapper<Object>) any()))
                .thenReturn(List.of(new NotificationResponse("Test Message", "Student Name")));
        List<NotificationResponse> result = notificationDao.getUnReadNotificationsForUser(1, "STUDENT");
        assertEquals(1, result.size());
        assertEquals("Test Message", result.get(0).getMessage());
        assertEquals("Student Name", result.get(0).getStudentName());
    }

    @Test
    public void testGetUnReadNotificationsForUser_Instructor() {
        when(jdbcTemplate.query(anyString(), any(Object[].class), (RowMapper<Object>) any()))
                .thenReturn(List.of(new NotificationResponse("Test Message", "Instructor Name")));
        List<NotificationResponse> result = notificationDao.getUnReadNotificationsForUser(1, "INSTRUCTOR");
        assertEquals(1, result.size());
        assertEquals("Test Message", result.get(0).getMessage());
        assertEquals("Instructor Name", result.get(0).getStudentName());
    }

    @Test
    public void testGetAllNotificationsForUser_Student() {
        when(jdbcTemplate.query(anyString(), any(Object[].class), (RowMapper<Object>) any()))
                .thenReturn(List.of(new NotificationResponse("Test Message", "Student Name")));
        List<NotificationResponse> result = notificationDao.getAllNotificationsForUser(1, "STUDENT");
        assertEquals(1, result.size());
        assertEquals("Test Message", result.get(0).getMessage());
        assertEquals("Student Name", result.get(0).getStudentName());
    }

    @Test
    public void testGetAllNotificationsForUser_Instructor() {
        when(jdbcTemplate.query(anyString(), any(Object[].class), (RowMapper<Object>) any()))
                .thenReturn(List.of(new NotificationResponse("Test Message", "Instructor Name")));
        List<NotificationResponse> result = notificationDao.getAllNotificationsForUser(1, "INSTRUCTOR");
        assertEquals(1, result.size());
        assertEquals("Test Message", result.get(0).getMessage());
        assertEquals("Instructor Name", result.get(0).getStudentName());
    }

    @Test
    public void testMarkAsRead_Student() {
        when(jdbcTemplate.update(anyString(), anyInt(), anyInt())).thenReturn(1);
        notificationDao.markAsRead(1, 1, "STUDENT");
        verify(jdbcTemplate, times(1)).update(anyString(), anyInt(), anyInt());
    }


    @Test
    public void testMarkAsRead_Instructor() {
        when(jdbcTemplate.update(anyString(), anyInt(), anyInt())).thenReturn(1);
        notificationDao.markAsRead(1, 1, "INSTRUCTOR");
        verify(jdbcTemplate, times(1)).update(anyString(), anyInt(), anyInt());
    }

}
