package com.project.LMS.dao.Assignments;

import com.project.LMS.dto.assignment.AssignmentGradeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AssignmentGradeDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final String INSERT_GRADE_FOR_ASSIGNMENT = "INSERT INTO Grades (id, assignment_id, student_id, score) VALUES(?, ?, ?, ?)";

    public void grade(AssignmentGradeRequest request) {
        jdbcTemplate.update(
                INSERT_GRADE_FOR_ASSIGNMENT,
                request.getGradeId(),
                request.getAssignmentId(),
                request.getStudentId(),
                request.getScore()
        );
    }
}
