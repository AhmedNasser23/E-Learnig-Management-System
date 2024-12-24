package com.project.LMS.service;
import com.project.LMS.entity.Course;
import com.project.LMS.entity.Enrollment;
import com.project.LMS.entity.Lesson;
import com.project.LMS.entity.StudentCourseKey;
import com.project.LMS.entity.users.MediaFile;
import com.project.LMS.repository.CourseRepository;
import com.project.LMS.repository.EnrollmentRepository;
import com.project.LMS.repository.LessonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class CourseServiceTest {
    @InjectMocks
    private CourseService courseService;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private EnrollmentRepository enrollmentRepository;
    @Mock
    private LessonRepository lessonRepository;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
//    @Test
//    void testCreateCourse() {
//        Course course = new Course();
//        course.setId(1);
//        when(courseRepository.save(any(Course.class))).thenReturn(course);
//        Course createdCourse = courseService.createCourse(course);
//        assertNotNull(createdCourse);
//        assertEquals(1, createdCourse.getId());
//        verify(courseRepository, times(1)).save(course);
//    }
//    @Test
//    void testDeleteCourse() {
//        Course course = new Course();
//        course.setId(1);
//        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
//        ResponseEntity<String> response = courseService.deleteCourse(1);
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals("Course with ID 1 has been deleted", response.getBody());
//        verify(courseRepository, times(1)).deleteById(1);
//    }
    @Test
    void testDeleteCourseNotFound() {
        when(courseRepository.findById(1)).thenReturn(Optional.empty());
        ResponseEntity<String> response = courseService.deleteCourse(1);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("invalid course id", response.getBody());
        verify(courseRepository, never()).deleteById(1);
    }
    @Test
    void testUpdateCourseTitle() {
        Course course = new Course();
        course.setId(1);
        course.setTitle("Old Title");
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        Course updatedCourse = courseService.updateCourseTitle(1, "New Title");
        assertNotNull(updatedCourse);
        assertEquals("New Title", updatedCourse.getTitle());
        verify(courseRepository, times(1)).save(course);
    }
    @Test
    void testUpdateCourseTitleNotFound() {
        when(courseRepository.findById(1)).thenReturn(Optional.empty());
        Course updatedCourse = courseService.updateCourseTitle(1, "New Title");
        assertNull(updatedCourse);
        verify(courseRepository, never()).save(any(Course.class));
    }
    @Test
    void testGetAllCourses() {
        List<Course> courses = new ArrayList<>();
        Course course1 = new Course();
        course1.setId(1);
        course1.setTitle("Course 1");
        course1.setDescription("Description 1");
        courses.add(course1);
        when(courseRepository.findAll()).thenReturn(courses);
        String result = courseService.getAllCourses();
        assertTrue(result.contains("Course ID: 1"));
        assertTrue(result.contains("Course Title: Course 1"));
        verify(courseRepository, times(1)).findAll();
    }
    @Test
    void testGetCourseLessons() {
        Course course = new Course();
        course.setId(1);
        Lesson lesson = new Lesson();
        lesson.setId(1);
        lesson.setTitle("Lesson 1");
        lesson.setMediaFiles(List.of(new MediaFile("file1"), new MediaFile("file2")));
        course.setLessons(List.of(lesson));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        ResponseEntity<String> response = courseService.getCourseLessons(1);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Lesson ID: 1"));
        assertTrue(response.getBody().contains("Lesson title: Lesson 1"));
        verify(courseRepository, times(1)).findById(1);
    }
    @Test
    void testGetCourseLessonsNotFound() {
        when(courseRepository.findById(1)).thenReturn(Optional.empty());
        ResponseEntity<String> response = courseService.getCourseLessons(1);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("invalid course id", response.getBody());
        verify(courseRepository, times(1)).findById(1);
    }
    @Test
    void testDisplayFileFileExists() {
        Course course = new Course();
        course.setId(1);
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(lessonRepository.findById(1)).thenReturn(Optional.of(new Lesson()));
        when(enrollmentRepository.findById(new StudentCourseKey(1, 1))).thenReturn(Optional.of(new Enrollment()));
        File file = mock(File.class);
        when(file.exists()).thenReturn(true);
        ResponseEntity response = courseService.displayFile("file.txt", 1, 1, 1);
        assertEquals(200, response.getStatusCodeValue());
        verify(courseRepository, times(1)).findById(1);
        verify(lessonRepository, times(1)).findById(1);
        verify(enrollmentRepository, times(1)).findById(new StudentCourseKey(1, 1));
    }
    @Test
    void testDisplayFileNotExists() {
        Course course = new Course();
        course.setId(1);
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(lessonRepository.findById(1)).thenReturn(Optional.of(new Lesson()));
        when(enrollmentRepository.findById(new StudentCourseKey(1, 1))).thenReturn(Optional.of(new Enrollment()));
        File file = mock(File.class);
        when(file.exists()).thenReturn(false);
        ResponseEntity response = courseService.displayFile("file.txt", 1, 1, 1);
        assertEquals(404, response.getStatusCodeValue());
        verify(courseRepository, times(1)).findById(1);
        verify(lessonRepository, times(1)).findById(1);
        verify(enrollmentRepository, times(1)).findById(new StudentCourseKey(1, 1));
    }
}