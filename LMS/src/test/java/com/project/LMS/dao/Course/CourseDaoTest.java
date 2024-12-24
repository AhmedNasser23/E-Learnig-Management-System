package com.project.LMS.dao.Course;

import com.project.LMS.entity.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.eq;

public class CourseDaoTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private CourseDao courseDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddCourse() {
        Course course = new Course();
        course.setDescription("Introduction to Programming");
        course.setDuration(12.5);
        course.setTitle("Programming 101");
        courseDao.addCourse(course);
        verify(jdbcTemplate).update(
                eq("INSERT INTO Courses (description, duration, title) VALUES (?, ?, ?)"),
                eq(course.getDescription()),
                eq(course.getDuration()),
                eq(course.getTitle())
        );
    }

    @Test
    public void testDeleteCourse() {
        Integer courseId = 1;
        courseDao.deleteCourse(courseId);
        verify(jdbcTemplate).update(
                eq("DELETE FROM Courses WHERE id = ?"),
                eq(courseId)
        );
    }
}
