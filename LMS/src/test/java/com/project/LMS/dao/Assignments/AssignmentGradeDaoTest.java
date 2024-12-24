package com.project.LMS.dao.Assignments;

import com.project.LMS.dto.assignment.AssignmentGradeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.eq;

public class AssignmentGradeDaoTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private AssignmentGradeDao assignmentGradeDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testGrade() {
        AssignmentGradeRequest request = new AssignmentGradeRequest();
        request.setGradeId(1);
        request.setAssignmentId(101);
        request.setStudentId(1001);
        request.setScore(95);

        assignmentGradeDao.grade(request);
        verify(jdbcTemplate).update(
                eq("INSERT INTO Grades (id, assignment_id, student_id, score) VALUES(?, ?, ?, ?)"),
                eq(request.getGradeId()),
                eq(request.getAssignmentId()),
                eq(request.getStudentId()),
                eq(request.getScore())
        );
    }
}
