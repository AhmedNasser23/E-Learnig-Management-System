package com.project.LMS.dao.Assignments;

import com.project.LMS.dto.assignment.AssignmentRequest;
import com.project.LMS.dto.assignment.AssignmentSubmissionsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AssignmentDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_ASSIGMENT_SQL = "INSERT INTO Assignments (title, course_id) VALUES (?, ?)";
    private static final String SELECT_ASSIGNMENTS_SQL =
            "SELECT Assignments.title AS assignment_name, Students.name AS student_name " +
                    "FROM AssignmentSubmissions " +
                    "JOIN Assignments ON AssignmentSubmissions.assignment_id = Assignments.id " +
                    "JOIN Students ON AssignmentSubmissions.student_id = Students.id " +
                    "WHERE AssignmentSubmissions.assignment_id = ?";
    private static final String SELECT_ASSIGNMENT_NAME_SQL = "SELECT title FROM Assignments WHERE assignment_id = ?";

    public void addAssigment(AssignmentRequest request) {
        jdbcTemplate.update(INSERT_ASSIGMENT_SQL, request.getTitle(), request.getCourseId());
    }

    public List<AssignmentSubmissionsResponse> getAssignmentSubmissions(int assignmentId) {
        return jdbcTemplate.query(
                SELECT_ASSIGNMENTS_SQL,
                new Object[]{assignmentId},
                (rs, rowNum) -> mapToAssignmentSubmissionsResponse(rs)
        );
    }

    private AssignmentSubmissionsResponse mapToAssignmentSubmissionsResponse(ResultSet rs) throws SQLException {
        AssignmentSubmissionsResponse response = new AssignmentSubmissionsResponse();
        response.setAssignmentName(rs.getString("assignment_name")); // Match query alias
        response.setStudentName(rs.getString("student_name"));       // Match query alias
        return response;
    }

    public String getAssignmentName(Integer id) {
        return jdbcTemplate.queryForObject(
                SELECT_ASSIGNMENT_NAME_SQL,
                new Object[]{id},
                String.class
        );
    }

}
