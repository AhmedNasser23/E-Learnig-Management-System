package com.project.LMS.dao.Notification;

import com.project.LMS.dto.notifications.NotificationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NotificationDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_STUDENT_NOTIFICATION_SQL =
            "INSERT INTO StudentNotifications (message, student_id) VALUES (?, ?)";

    private static final String INSERT_INSTRUCTOR_NOTIFICATION_SQL =
            "INSERT INTO InstructorNotifications (message, instructor_id) VALUES (?, ?)";

    private static final String FETCH_STUDENT_NOTIFICATIONS_SQL =
            "SELECT Students.name AS name, StudentNotifications.message AS message " +
                    "FROM StudentNotifications " +
                    "JOIN Students ON StudentNotifications.studend_id = Students.id " +
                    "WHERE Students.id = ? AND Notfications.isRead = 0";

    private static final String FETCH_INSTRUCTOR_NOTIFICATIONS_SQL =
            "SELECT Instructors.name AS name, InstructorNotifications.message AS message " +
                    "FROM InstructorNotifications " +
                    "JOIN Instructors ON InstructorNotifications.instructor_id = Instructors.id " +
                    "WHERE Instructors.id = ? AND InstructorNotifications.isRead = 0";


    private static final String FETCH_STUDENT_UNREAD_NOTIFICATIONS_SQL =
            "SELECT Students.name AS name, StudentNotifications.message AS message " +
                    "FROM StudentNotifications " +
                    "JOIN Students ON StudentNotifications.studend_id = Students.id " +
                    "WHERE Students.id = ?";

    private static final String FETCH_INSTRUCTOR_UNREAD_NOTIFICATIONS_SQL =
            "SELECT Instructors.name AS name, InstructorNotifications.message AS message " +
                    "FROM InstructorNotifications " +
                    "JOIN Instructors ON InstructorNotifications.instructor_id = Instructors.id " +
                    "WHERE Instructors.id = ?";

    private static final String MARK_STUDENT_NOTIFICATION_AS_READ = "UPDATE  StudentNotifications" +
            "SET isRead = TRUE " +
            "WHERE student_id = ? AND Id = ?";

    private static final String MARK_INSTRUCTOR_NOTIFICATION_AS_READ = "UPDATE Notifications " +
            "SET isRead = TRUE " +
            "WHERE instructor_id = ? AND Id = ?";

    public void createMessageForUser(int userId, String  message, String  role) {
        switch (role) {
            case "STUDENT":
                jdbcTemplate.update(INSERT_STUDENT_NOTIFICATION_SQL, userId, message);
                break;
            case "INSTRUCTOR":
                jdbcTemplate.update(INSERT_INSTRUCTOR_NOTIFICATION_SQL, userId, message);
                break;
        }
    }

    public List<NotificationResponse> getUnReadNotificationsForUser(int userId, String role) {

        if (role == null)
            return null;

        switch (role) {
            case "STUDENT":
                return jdbcTemplate.query(FETCH_STUDENT_NOTIFICATIONS_SQL, new Object[]{userId}, (rs, rowNum) -> {
                    NotificationResponse response = new NotificationResponse();
                    response.setMessage(rs.getString("message"));
                    response.setStudentName(rs.getString("name"));
                    return response;
                });
            case "INSTRUCTOR":
                return jdbcTemplate.query(FETCH_INSTRUCTOR_NOTIFICATIONS_SQL, new Object[]{userId}, (rs, rowNum) -> {
                    NotificationResponse response = new NotificationResponse();
                    response.setMessage(rs.getString("message"));
                    response.setStudentName(rs.getString("name"));
                    return response;
                });
        }

        return null;
    }

    public List<NotificationResponse> getAllNotificationsForUser(int userId, String role) {

        switch (role) {
            case "STUDENT":
                return jdbcTemplate.query(FETCH_STUDENT_UNREAD_NOTIFICATIONS_SQL, new Object[]{userId}, (rs, rowNum) -> {
                    NotificationResponse response = new NotificationResponse();
                    response.setMessage(rs.getString("message"));
                    response.setStudentName(rs.getString("name"));
                    return response;
                });
            case "INSTRUCTOR":
                return jdbcTemplate.query(FETCH_INSTRUCTOR_UNREAD_NOTIFICATIONS_SQL, new Object[]{userId}, (rs, rowNum) -> {
                    NotificationResponse response = new NotificationResponse();
                    response.setMessage(rs.getString("message"));
                    response.setStudentName(rs.getString("name"));
                    return response;
                });
        }

        return null;
    }

    public void markAsRead(int userId, int notificationId, String role) {
        switch (role) {
            case "STUDENT":
                jdbcTemplate.update(MARK_STUDENT_NOTIFICATION_AS_READ, userId, notificationId);
                break;
            case "INSTRUCTOR":
                jdbcTemplate.update(MARK_INSTRUCTOR_NOTIFICATION_AS_READ, userId, notificationId);
                break;
        }
    }
}
