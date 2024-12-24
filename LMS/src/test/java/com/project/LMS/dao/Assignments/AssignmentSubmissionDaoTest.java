package com.project.LMS.dao.Assignments;

import com.project.LMS.dto.assignment.AssignmentSubmissionRequestBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.eq;

public class AssignmentSubmissionDaoTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private AssignmentSubmissionDao assignmentSubmissionDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSubmitAssignment() {
        AssignmentSubmissionRequestBody body = new AssignmentSubmissionRequestBody();
        body.setAssignmentId(1);
        body.setStudentId(101);
        body.setFileURL("http://example.com/file");

        assignmentSubmissionDao.submitAssignment(body);
        verify(jdbcTemplate).update(
                eq("INSERT INTO AssignmentSubmissions (assignment_id, student_id, fileUrl) VALUES ( ?, ?, ?)"),
                eq(body.getAssignmentId()),
                eq(body.getStudentId()),
                eq(body.getFileURL())
        );
    }
}
