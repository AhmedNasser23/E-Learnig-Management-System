package com.project.LMS.dao.Users;

import com.project.LMS.entity.users.Student;
import com.project.LMS.entity.users.userRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.project.LMS.entity.users.User;

@Repository
public class StudentDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_USER_SQL = "INSERT INTO Students (name, email, password, profile_info, role) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_STUDENT_BY_ID_SQL = "SELECT * FROM Students WHERE id = ?";
    private static final String DELETE_STUDENT_SQL = "DELETE FROM Students WHERE id = ?";
    private static final String SELECT_STUDENT_BY_EMAIL = "SELECT * FROM Students WHERE email = ?";

    public void addStudent(User student) {
        jdbcTemplate.update(
                INSERT_USER_SQL,
                student.getName(),
                student.getEmail(),
                student.getPassword(),
                student.getProfileInfo(),
                student.getRole().toString()
        );
    }

    public Student findById(Integer id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_STUDENT_BY_ID_SQL, new Object[]{id}, (rs, rowNum) -> {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setName(rs.getString("name"));
                student.setPassword(rs.getString("password"));
                student.setEmail(rs.getString("email"));
                student.setProfileInfo(rs.getString("profile_info"));
                student.setRole(userRole.valueOf(rs.getString("role")));
                return student;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    public void deleteStudent(Integer id) {
        jdbcTemplate.update(DELETE_STUDENT_SQL, id);
    }

    public Student getByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject(SELECT_STUDENT_BY_EMAIL, new Object[]{email}, (rs, rowNum) -> {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setName(rs.getString("name"));
                student.setPassword(rs.getString("password"));
                student.setEmail(rs.getString("email"));
                student.setProfileInfo(rs.getString("profile_info"));
                student.setRole(userRole.valueOf(rs.getString("role")));
                return student;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
