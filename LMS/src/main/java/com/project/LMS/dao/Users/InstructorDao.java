package com.project.LMS.dao.Users;

import com.project.LMS.entity.users.Instructor;
import com.project.LMS.entity.users.Student;
import com.project.LMS.entity.users.userRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.project.LMS.entity.users.User;

@Repository
public class InstructorDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_INSTRUCTOR_SQL = "INSERT INTO Instructors (name, password, email, profile_info, role) VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE_INSTRUCTOR_SQL = "DELETE FROM Instructors WHERE id = ?";
    private static final String SELECT_INSTRUCTOR_BY_EMAIL = "SELECT * FROM Instructors WHERE email = ?";

    public void addInstructor(User instructor) {
        jdbcTemplate.update(INSERT_INSTRUCTOR_SQL, instructor.getName(), instructor.getPassword(), instructor.getEmail(), instructor.getProfileInfo(), instructor.getRole().toString());
    }
    public void deleteInstructor(Integer id) {
        jdbcTemplate.update(DELETE_INSTRUCTOR_SQL, id);
    }
    public Instructor getByEmail(String email) {
        return jdbcTemplate.queryForObject(SELECT_INSTRUCTOR_BY_EMAIL, new Object[]{email}, (rs, rowNum) -> {
            Instructor instructor = new Instructor();
            instructor.setId(rs.getInt("id"));
            instructor.setName(rs.getString("name"));
            instructor.setPassword(rs.getString("password"));
            instructor.setEmail(rs.getString("email"));
            instructor.setProfileInfo(rs.getString("profile_info"));
            instructor.setRole(userRole.valueOf(rs.getString("role")));
            return instructor;
        });
    }
}
