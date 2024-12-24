package com.project.LMS.controller;

import com.project.LMS.dao.Course.CourseDao;
import com.project.LMS.dto.assignment.AssignmentSubmissionRequestBody;
import com.project.LMS.dto.assignment.AssignmentsGradesDto;
import com.project.LMS.dto.enrollment.EnrollmentRequest;
import com.project.LMS.dto.material.MaterialDto;
import com.project.LMS.dto.quiz.QuizGradeDto;
import com.project.LMS.service.AssignmentSubmissionService;
import com.project.LMS.service.EnrollmentService;
import com.project.LMS.service.NotificationService;
import com.project.LMS.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final AssignmentSubmissionService assignmentSubmissionService;
    private final NotificationService notificationService;
    private final CourseDao courseDao;
    private EnrollmentService enrollmentService;


    @Autowired
    public StudentController(StudentService studentService, AssignmentSubmissionService assignmentSubmissionService, NotificationService notificationService, CourseDao courseDao) {
        this.studentService = studentService;
        this.assignmentSubmissionService = assignmentSubmissionService;
        this.notificationService = notificationService;
        this.courseDao = courseDao;
    }

    @PostMapping("/enroll")
    public ResponseEntity<String> enrollInCourse(@RequestBody EnrollmentRequest request) {
        try {
            studentService.enrollInCourse(request.getStudentId(), request.getCourseId());
            enrollmentService.enroll(request.getStudentId(), request.getCourseId());
            String courseName = courseDao.getCourseName(request.getCourseId());
            notificationService.sendNotificationToUser(courseDao.getInstructorId(request.getCourseId()), "Request to enroll in course: " + courseName, "INSTRUCTOR");

            return ResponseEntity.status(HttpStatus.CREATED).body("Enrollment successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while enrolling in the course: " + e.getMessage());
        }

    }

    @GetMapping("/grades/assignments")
    public ResponseEntity<?> getAssignmentGrades(@RequestParam int studentId) {
        try {
            List<AssignmentsGradesDto> grades = studentService.getAssignmentGradesByStudentId(studentId);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching assignment grades: " + e.getMessage());
        }
    }

    @GetMapping("/grades/quizzes")
    public ResponseEntity<?> getQuizGrades(@RequestParam int studentId) {
        try {
            List<QuizGradeDto> grades = studentService.getQuizGradesByStudentId(studentId);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching quiz grades: " + e.getMessage());
        }
    }

    @GetMapping("/materials")
    public ResponseEntity<?> getMaterials(@RequestParam Integer courseId) {
        try {
            List<MaterialDto> materials = studentService.getMaterialsByCourseId(courseId);
            return ResponseEntity.ok(materials);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching materials: " + e.getMessage());
        }
    }

    @PostMapping("/submitAssignment")
    public ResponseEntity<String> submitAssignment(@RequestBody AssignmentSubmissionRequestBody body) {
        try {
            assignmentSubmissionService.submitAssignment(body);
            return ResponseEntity.status(HttpStatus.CREATED).body("Assignment submitted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while submitting the assignment: " + e.getMessage());
        }
    }
}
