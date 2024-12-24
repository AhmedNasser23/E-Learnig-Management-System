package com.project.LMS.service;

import com.project.LMS.entity.Course;
import com.project.LMS.entity.Enrollment;
import com.project.LMS.entity.StudentCourseKey;
import com.project.LMS.entity.users.Student;
import com.project.LMS.repository.CourseRepository;
import com.project.LMS.repository.EnrollmentRepository;
import com.project.LMS.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentService {
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;

    public ResponseEntity<String> enroll(Integer studentId, Integer courseId) {
        // check if studentId not exist
        if (studentRepository.findById(studentId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("failed to enroll in course with id: " + courseId + "\nstudent id: " + studentId + " does not exist");
        }
        // check if courseId not exist
        if (courseRepository.findById(courseId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("failed to enroll in course with id: " + courseId + "\ncourse id: " + courseId + " does not exist");
        }
        // check if studentId already enrolled in courseId
        // conflict -> 409 status code
        if (enrollmentRepository.findById(new StudentCourseKey(studentId, courseId)).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("failed to enroll in course with id: " + courseId + "\nstudent with id: " + studentId + " is already enrolled in course with id: " + courseId);
        }

        // Create and save the enrollment
        Enrollment enrollment = new Enrollment();
        Student student = studentRepository.findById(studentId).get();
        Course course = courseRepository.findById(courseId).get();

        enrollment.setId(new StudentCourseKey(studentId, courseId));
        enrollment.setStudent(student);
        enrollment.setCourse(course);

        enrollmentRepository.save(enrollment);
        return ResponseEntity.ok("student with id " + studentId + " enrolled in course with id " + courseId + " successfully");
    }

    public ResponseEntity<String> unenroll(Integer studentId,Integer courseId) {
        // check if studentId not exist
        if (studentRepository.findById(studentId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("failed to unenroll from course with id: " + courseId + "\ninvalid student id");
        }
        // check if courseId not exist
        if (courseRepository.findById(courseId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("failed to unenroll from course with id: " + courseId + "\ninvalid course id");
        }
        // check if studentId already unenrolled from courseId
        if (enrollmentRepository.findById(new StudentCourseKey(studentId, courseId)).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("failed to unenroll from course with id: " + courseId + "\nstudent with id: " + studentId + " is already unenrolled from course with id: " + courseId);
        }
        // un enroll
        enrollmentRepository.deleteById(new StudentCourseKey(studentId, courseId));
        return ResponseEntity.ok("student with id " + studentId + " unenrolled from course with id " + courseId + " successfully");
    }

}