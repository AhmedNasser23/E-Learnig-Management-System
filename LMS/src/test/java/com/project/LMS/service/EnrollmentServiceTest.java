package com.project.LMS.service;
import com.project.LMS.entity.Course;
import com.project.LMS.entity.Enrollment;
import com.project.LMS.entity.StudentCourseKey;
import com.project.LMS.entity.users.Student;
import com.project.LMS.repository.CourseRepository;
import com.project.LMS.repository.EnrollmentRepository;
import com.project.LMS.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class EnrollmentServiceTest {
    @InjectMocks
    private EnrollmentService enrollmentService;
    @Mock
    private EnrollmentRepository enrollmentRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private CourseRepository courseRepository;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testEnrollSuccess() {
        Integer studentId = 1;
        Integer courseId = 1;
        Student student = new Student();
        student.setId(studentId);
        Course course = new Course();
        course.setId(courseId);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(enrollmentRepository.findById(new StudentCourseKey(studentId, courseId))).thenReturn(Optional.empty());
        ResponseEntity<String> response = enrollmentService.enroll(studentId, courseId);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("student with id 1 enrolled in course with id 1 successfully", response.getBody());
        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }
    @Test
    void testEnrollStudentNotFound() {
        Integer studentId = 1;
        Integer courseId = 1;
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
        ResponseEntity<String> response = enrollmentService.enroll(studentId, courseId);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("failed to enroll in course with id: 1\nstudent id: 1 does not exist", response.getBody());
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }
    @Test
    void testEnrollCourseNotFound() {
        Integer studentId = 1;
        Integer courseId = 1;
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(new Student()));
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());
        ResponseEntity<String> response = enrollmentService.enroll(studentId, courseId);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("failed to enroll in course with id: 1\ncourse id: 1 does not exist", response.getBody());
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }
    @Test
    void testEnrollConflict() {
        Integer studentId = 1;
        Integer courseId = 1;
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(new Student()));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(new Course()));
        when(enrollmentRepository.findById(new StudentCourseKey(studentId, courseId))).thenReturn(Optional.of(new Enrollment()));
        ResponseEntity<String> response = enrollmentService.enroll(studentId, courseId);
        assertEquals(409, response.getStatusCodeValue());
        assertEquals("failed to enroll in course with id: 1\nstudent with id: 1 is already enrolled in course with id: 1", response.getBody());
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }
    @Test
    void testUnenrollSuccess() {
        Integer studentId = 1;
        Integer courseId = 1;
        Student student = new Student();
        student.setId(studentId);
        Course course = new Course();
        course.setId(courseId);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(enrollmentRepository.findById(new StudentCourseKey(studentId, courseId))).thenReturn(Optional.of(new Enrollment()));
        ResponseEntity<String> response = enrollmentService.unenroll(studentId, courseId);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("student with id 1 unenrolled from course with id 1 successfully", response.getBody());
        verify(enrollmentRepository, times(1)).deleteById(new StudentCourseKey(studentId, courseId));
    }
    @Test
    void testUnenrollStudentNotFound() {
        Integer studentId = 1;
        Integer courseId = 1;
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
        ResponseEntity<String> response = enrollmentService.unenroll(studentId, courseId);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("failed to unenroll from course with id: 1\ninvalid student id", response.getBody());
        verify(enrollmentRepository, never()).deleteById(new StudentCourseKey(studentId, courseId));
    }
    @Test
    void testUnenrollCourseNotFound() {
        Integer studentId = 1;
        Integer courseId = 1;
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(new Student()));
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());
        ResponseEntity<String> response = enrollmentService.unenroll(studentId, courseId);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("failed to unenroll from course with id: 1\ninvalid course id", response.getBody());
        verify(enrollmentRepository, never()).deleteById(new StudentCourseKey(studentId, courseId));
    }
    @Test
    void testUnenrollNotEnrolled() {
        Integer studentId = 1;
        Integer courseId = 1;
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(new Student()));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(new Course()));
        when(enrollmentRepository.findById(new StudentCourseKey(studentId, courseId))).thenReturn(Optional.empty());
        ResponseEntity<String> response = enrollmentService.unenroll(studentId, courseId);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("failed to unenroll from course with id: 1\nstudent with id: 1 is already unenrolled from course with id: 1", response.getBody());
        verify(enrollmentRepository, never()).deleteById(new StudentCourseKey(studentId, courseId));
    }
}