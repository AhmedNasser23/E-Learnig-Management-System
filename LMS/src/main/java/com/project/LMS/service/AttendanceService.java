package com.project.LMS.service;
import com.project.LMS.entity.Attendance;
import com.project.LMS.entity.Lesson;
import com.project.LMS.entity.StudentLessonKey;
import com.project.LMS.entity.users.Student;
import com.project.LMS.repository.AttendanceRepository;
import com.project.LMS.repository.LessonRepository;
import com.project.LMS.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private LessonRepository lessonRepository;

    public ResponseEntity<String> recordAttendance(Integer studentId, Integer lessonId, String otp) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        Optional<Lesson> optionalLesson = lessonRepository.findById(lessonId);
        if (optionalStudent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("invalid student id");
        }
        if (optionalLesson.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("invalid lesson id");
        }
        if (attendanceRepository.findById(new StudentLessonKey(studentId, lessonId)).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Student with ID " + studentId + " is already recorded attendance in lesson with ID " + lessonId);
        }
        if (!(optionalLesson.get().getOtp().equals(otp))) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("invalid OTP");
        }
        if (optionalLesson.get().getOtpExpiration().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP Expired");
        }

        Attendance attendance = new Attendance();
        Student student = optionalStudent.get();
        Lesson lesson = optionalLesson.get();
        attendance.setId(new StudentLessonKey(studentId, lessonId));
        attendance.setStudent(student);
        attendance.setLesson(lesson);
        attendanceRepository.save(attendance);
        return ResponseEntity.ok("Student with ID " + studentId + " recorded attendance in lesson with ID " + lessonId);
    }

}