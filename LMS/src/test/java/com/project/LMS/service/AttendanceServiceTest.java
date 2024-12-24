package com.project.LMS.service;
import com.project.LMS.entity.Attendance;
import com.project.LMS.entity.Lesson;
import com.project.LMS.entity.StudentLessonKey;
import com.project.LMS.entity.users.Student;
import com.project.LMS.repository.AttendanceRepository;
import com.project.LMS.repository.LessonRepository;
import com.project.LMS.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class AttendanceServiceTest {
    @InjectMocks
    private AttendanceService attendanceService;
    @Mock
    private AttendanceRepository attendanceRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private LessonRepository lessonRepository;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testRecordAttendanceSuccess() {
        Integer studentId = 1;
        Integer lessonId = 1;
        String otp = "1234";
        Student student = new Student();
        student.setId(studentId);
        Lesson lesson = new Lesson();
        lesson.setId(lessonId);
        lesson.setOtp(otp);
        lesson.setOtpExpiration(LocalDateTime.now().plusMinutes(5));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
        when(attendanceRepository.findById(new StudentLessonKey(studentId, lessonId))).thenReturn(Optional.empty());
        ResponseEntity<String> response = attendanceService.recordAttendance(studentId, lessonId, otp);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Student with ID 1 recorded attendance in lesson with ID 1", response.getBody());
        verify(attendanceRepository, times(1)).save(any(Attendance.class));
    }
    @Test
    void testRecordAttendanceInvalidStudentId() {
        Integer studentId = 1;
        Integer lessonId = 1;
        String otp = "1234";
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
        ResponseEntity<String> response = attendanceService.recordAttendance(studentId, lessonId, otp);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("invalid student id", response.getBody());
        verify(attendanceRepository, never()).save(any(Attendance.class));
    }
    @Test
    void testRecordAttendanceInvalidLessonId() {
        Integer studentId = 1;
        Integer lessonId = 1;
        String otp = "1234";
        Student student = new Student();
        student.setId(studentId);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());
        ResponseEntity<String> response = attendanceService.recordAttendance(studentId, lessonId, otp);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("invalid lesson id", response.getBody());
        verify(attendanceRepository, never()).save(any(Attendance.class));
    }
    @Test
    void testRecordAttendanceAlreadyRecorded() {
        Integer studentId = 1;
        Integer lessonId = 1;
        String otp = "1234";
        Student student = new Student();
        student.setId(studentId);
        Lesson lesson = new Lesson();
        lesson.setId(lessonId);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
        when(attendanceRepository.findById(new StudentLessonKey(studentId, lessonId))).thenReturn(Optional.of(new Attendance()));
        ResponseEntity<String> response = attendanceService.recordAttendance(studentId, lessonId, otp);
        assertEquals(409, response.getStatusCodeValue());
        assertEquals("Student with ID 1 is already recorded attendance in lesson with ID 1", response.getBody());
        verify(attendanceRepository, never()).save(any(Attendance.class));
    }
    @Test
    void testRecordAttendanceInvalidOtp() {
        Integer studentId = 1;
        Integer lessonId = 1;
        String otp = "1234";
        Student student = new Student();
        student.setId(studentId);
        Lesson lesson = new Lesson();
        lesson.setId(lessonId);
        lesson.setOtp("5678");
        lesson.setOtpExpiration(LocalDateTime.now().plusMinutes(5));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
        ResponseEntity<String> response = attendanceService.recordAttendance(studentId, lessonId, otp);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("invalid OTP", response.getBody());
        verify(attendanceRepository, never()).save(any(Attendance.class));
    }
    @Test
    void testRecordAttendanceExpiredOtp() {
        Integer studentId = 1;
        Integer lessonId = 1;
        String otp = "1234";
        Student student = new Student();
        student.setId(studentId);
        Lesson lesson = new Lesson();
        lesson.setId(lessonId);
        lesson.setOtp(otp);
        lesson.setOtpExpiration(LocalDateTime.now().minusMinutes(5));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
        ResponseEntity<String> response = attendanceService.recordAttendance(studentId, lessonId, otp);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("OTP Expired", response.getBody());
        verify(attendanceRepository, never()).save(any(Attendance.class));
    }
}