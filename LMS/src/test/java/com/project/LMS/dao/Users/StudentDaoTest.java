package com.project.LMS.dao.Users;

import com.project.LMS.entity.users.Student;
import com.project.LMS.entity.users.User;
import com.project.LMS.entity.users.userRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentDaoTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private StudentDao studentDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddStudent() {
        User student = new User();
        student.setName("Alice");
        student.setEmail("alice@example.com");
        student.setPassword("securePassword123");
        student.setProfileInfo("Profile Info");
        student.setRole(userRole.STUDENT);

        studentDao.addStudent(student);

        verify(jdbcTemplate).update(
                eq("INSERT INTO Students (name, email, password, profile_info, role) VALUES (?, ?, ?, ?, ?)"),
                eq("Alice"),
                eq("alice@example.com"),
                eq("securePassword123"),
                eq("Profile Info"),
                eq(userRole.STUDENT.toString())
        );
    }

    @Test
    void testFindByIdWhenStudentExists() {
        int studentId = 1;
        Student mockStudent = new Student();
        mockStudent.setId(studentId);
        mockStudent.setName("Alice");
        mockStudent.setEmail("alice@example.com");
        mockStudent.setPassword("securePassword123");
        mockStudent.setProfileInfo("Profile Info");
        mockStudent.setRole(userRole.STUDENT);

        when(jdbcTemplate.queryForObject(
                eq("SELECT * FROM Students WHERE id = ?"),
                any(Object[].class),
                (RowMapper<Object>) any()
        )).thenReturn(mockStudent);

        Student student = studentDao.findById(studentId);

        assertNotNull(student);
        assertEquals(studentId, student.getId());
        assertEquals("Alice", student.getName());
        assertEquals("alice@example.com", student.getEmail());
        assertEquals("securePassword123", student.getPassword());
        assertEquals("Profile Info", student.getProfileInfo());
        assertEquals(userRole.STUDENT, student.getRole());
    }

    @Test
    void testFindByIdWhenStudentDoesNotExist() {
        int studentId = 1;

        when(jdbcTemplate.queryForObject(
                eq("SELECT * FROM Students WHERE id = ?"),
                any(Object[].class),
                (RowMapper<Object>) any()
        )).thenThrow(new EmptyResultDataAccessException(1));

        Student student = studentDao.findById(studentId);

        assertNull(student);
    }


    @Test
    void testDeleteStudent() {
        int studentId = 1;

        studentDao.deleteStudent(studentId);

        verify(jdbcTemplate).update(
                eq("DELETE FROM Students WHERE id = ?"),
                eq(studentId)
        );
    }

    @Test
    void testGetByEmailWhenStudentExists() {
        String email = "alice@example.com";
        Student mockStudent = new Student();
        mockStudent.setId(1);
        mockStudent.setName("Alice");
        mockStudent.setEmail(email);
        mockStudent.setPassword("securePassword123");
        mockStudent.setProfileInfo("Profile Info");
        mockStudent.setRole(userRole.STUDENT);

        when(jdbcTemplate.queryForObject(
                eq("SELECT * FROM Students WHERE email = ?"),
                any(Object[].class),
                (RowMapper<Object>) any()
        )).thenReturn(mockStudent);

        Student student = studentDao.getByEmail(email);

        assertNotNull(student);
        assertEquals(1, student.getId());
        assertEquals("Alice", student.getName());
        assertEquals(email, student.getEmail());
        assertEquals("securePassword123", student.getPassword());
        assertEquals("Profile Info", student.getProfileInfo());
        assertEquals(userRole.STUDENT, student.getRole());
    }

    @Test
    void testGetByEmailWhenStudentDoesNotExist() {
        String email = "nonexistent@example.com";

        when(jdbcTemplate.queryForObject(
                eq("SELECT * FROM Students WHERE email = ?"),
                any(Object[].class),
                (RowMapper<Object>) any()
        )).thenThrow(new EmptyResultDataAccessException(1));

        Student student = studentDao.getByEmail(email);

        assertNull(student);
    }
}
