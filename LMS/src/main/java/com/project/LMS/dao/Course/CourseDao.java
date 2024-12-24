package com.project.LMS.dao.Course;

import com.project.LMS.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CourseDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_COURSE_SQL = "INSERT INTO Courses (description, duration, title) VALUES (?, ?, ?)";
    private static final String DELETE_COURSE_SQL = "DELETE FROM Courses WHERE id = ?";
    private static final String SELECT_INSTRUCTOR_OF_THE_COURSE_SQL = "SELECT instructor_id FROM Courses WHERE id = ?";
    private static final String SELECT_COURSE_NAME_SQL = "SELECT title FROM Courses WHERE id = ?";

    public void addCourse(Course course) {
        jdbcTemplate.update(INSERT_COURSE_SQL, course.getDescription(), course.getDuration(), course.getTitle());
    }

    public void deleteCourse(Integer id) {
        jdbcTemplate.update(DELETE_COURSE_SQL, id);
    }

    public int getInstructorId(Integer id) {
        return jdbcTemplate.queryForObject(
                SELECT_INSTRUCTOR_OF_THE_COURSE_SQL,
                new Object[]{id},
                Integer.class
        );
    }

    public String getCourseName(Integer id) {
        return jdbcTemplate.queryForObject(
                SELECT_COURSE_NAME_SQL,
                new Object[]{id},
                String.class
        );
    }
}
