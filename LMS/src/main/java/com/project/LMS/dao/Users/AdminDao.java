package com.project.LMS.dao.Users;

import com.project.LMS.entity.users.Admin;
import com.project.LMS.entity.users.userRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.project.LMS.entity.users.User;

@Repository
public class AdminDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final String INSERT_ADMIN_SQL = "INSERT INTO Admins (name, password, email, profile_info, role) VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE_ADMIN_SQL = "DELETE FROM Admins WHERE id = ?";
    private static final String SELECT_ADMIN_BY_EMAIL = "SELECT * FROM Admins WHERE email = ?";

    public void addAdmin(User admin) {
        jdbcTemplate.update(INSERT_ADMIN_SQL, admin.getName(), admin.getPassword(), admin.getEmail(), admin.getProfileInfo(), admin.getRole().toString());
    }
    public void deleteAdmin(Integer id) {
        jdbcTemplate.update(DELETE_ADMIN_SQL, id);
    }
    public Admin getByEmail(String email) {
        return jdbcTemplate.queryForObject(SELECT_ADMIN_BY_EMAIL, new Object[]{email}, (rs, rowNum) -> {
            Admin student = new Admin();
            student.setId(rs.getInt("id"));
            student.setName(rs.getString("name"));
            student.setPassword(rs.getString("password"));
            student.setEmail(rs.getString("email"));
            student.setProfileInfo(rs.getString("profile_info"));
            student.setRole(userRole.valueOf(rs.getString("role")));
            return student;
        });
    }
}
