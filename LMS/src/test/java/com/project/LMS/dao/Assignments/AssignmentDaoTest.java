package com.project.LMS.dao.Assignments;

import com.project.LMS.dao.Assignments.AssignmentDao;
import com.project.LMS.dto.assignment.AssignmentRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssignmentDaoTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private AssignmentDao assignmentDao;

    @Test
    void testAddAssignment() {
        AssignmentRequest request = new AssignmentRequest();
        request.setTitle("Math Assignment");
        request.setCourseId(1);

        assignmentDao.addAssigment(request);

        verify(jdbcTemplate, times(1)).update(
                "INSERT INTO Assignments (title, course_id) VALUES (?, ?)",
                request.getTitle(),
                request.getCourseId()
        );
    }
}
