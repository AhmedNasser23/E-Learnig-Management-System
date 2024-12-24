package com.project.LMS.dao.Users;

import com.project.LMS.entity.users.Admin;
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

class AdminDaoTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private AdminDao adminDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddAdmin() {
        User admin = new User();
        admin.setName("John Doe");
        admin.setPassword("password123");
        admin.setEmail("john.doe@example.com");
        admin.setProfileInfo("Admin Profile");
        admin.setRole(userRole.ADMIN);

        adminDao.addAdmin(admin);

        verify(jdbcTemplate).update(
                eq("INSERT INTO Admins (name, password, email, profile_info, role) VALUES (?, ?, ?, ?, ?)"),
                eq("John Doe"),
                eq("password123"),
                eq("john.doe@example.com"),
                eq("Admin Profile"),
                eq("ADMIN")
        );
    }

    @Test
    void testDeleteAdmin() {
        int adminId = 1;

        adminDao.deleteAdmin(adminId);

        verify(jdbcTemplate).update(
                eq("DELETE FROM Admins WHERE id = ?"),
                eq(adminId)
        );
    }

    @Test
    void testGetByEmail() {
        String email = "john.doe@example.com";
        Admin mockAdmin = new Admin();
        mockAdmin.setId(1);
        mockAdmin.setName("John Doe");
        mockAdmin.setPassword("password123");
        mockAdmin.setEmail(email);
        mockAdmin.setProfileInfo("Admin Profile");
        mockAdmin.setRole(userRole.ADMIN);

        when(jdbcTemplate.queryForObject(
                eq("SELECT * FROM Admins WHERE email = ?"),
                any(Object[].class),
                (RowMapper<Object>) any()
        )).thenReturn(mockAdmin);

        Admin admin = adminDao.getByEmail(email);

        assertNotNull(admin);
        assertEquals(1, admin.getId());
        assertEquals("John Doe", admin.getName());
        assertEquals("password123", admin.getPassword());
        assertEquals(email, admin.getEmail());
        assertEquals("Admin Profile", admin.getProfileInfo());
        assertEquals(userRole.ADMIN, admin.getRole());
    }
}
