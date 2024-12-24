package com.project.LMS.dao.Users;

import com.project.LMS.entity.users.Instructor;
import com.project.LMS.entity.users.User;
import com.project.LMS.entity.users.userRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class InstructorDaoTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private InstructorDao instructorDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddInstructor() {
        User instructor = new User();
        instructor.setName("Jane Doe");
        instructor.setPassword("securepass");
        instructor.setEmail("jane.doe@example.com");
        instructor.setProfileInfo("Experienced instructor");
        instructor.setRole(userRole.INSTRUCTOR);

        instructorDao.addInstructor(instructor);

        verify(jdbcTemplate).update(
                eq("INSERT INTO Instructors (name, password, email, profile_info, role) VALUES (?, ?, ?, ?, ?)"),
                eq("Jane Doe"),
                eq("securepass"),
                eq("jane.doe@example.com"),
                eq("Experienced instructor"),
                eq("INSTRUCTOR")
        );
    }

    @Test
    void testDeleteInstructor() {
        int instructorId = 5;

        instructorDao.deleteInstructor(instructorId);

        verify(jdbcTemplate).update(
                eq("DELETE FROM Instructors WHERE id = ?"),
                eq(instructorId)
        );
    }

    @Test
    void testGetByEmail() {
        String email = "jane.doe@example.com";
        Instructor mockInstructor = new Instructor();
        mockInstructor.setId(5);
        mockInstructor.setName("Jane Doe");
        mockInstructor.setPassword("securepass");
        mockInstructor.setEmail(email);
        mockInstructor.setProfileInfo("Experienced instructor");
        mockInstructor.setRole(userRole.INSTRUCTOR);

        when(jdbcTemplate.queryForObject(
                eq("SELECT * FROM Instructors WHERE email = ?"),
                any(Object[].class),
                (RowMapper<Object>) any()
        )).thenReturn(mockInstructor);

        Instructor instructor = instructorDao.getByEmail(email);

        assertNotNull(instructor);
        assertEquals(5, instructor.getId());
        assertEquals("Jane Doe", instructor.getName());
        assertEquals("securepass", instructor.getPassword());
        assertEquals(email, instructor.getEmail());
        assertEquals("Experienced instructor", instructor.getProfileInfo());
        assertEquals(userRole.INSTRUCTOR, instructor.getRole());
    }
}
