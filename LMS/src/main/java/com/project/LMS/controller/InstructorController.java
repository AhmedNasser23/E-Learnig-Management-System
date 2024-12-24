package com.project.LMS.controller;

import com.project.LMS.dao.Assignments.AssignmentDao;
import com.project.LMS.dao.Course.CourseDao;
import com.project.LMS.dto.assignment.AssignmentGradeRequest;
import com.project.LMS.dto.assignment.AssignmentRequest;
import com.project.LMS.dto.assignment.AssignmentSubmissionsResponse;
import com.project.LMS.dto.quiz.QuizGradeGetRequest;
import com.project.LMS.dto.quiz.QuizGradeRequest;
import com.project.LMS.dto.quiz.QuizRequest;
import com.project.LMS.entity.Course;
import com.project.LMS.service.AssignmentSubmissionService;
import com.project.LMS.service.InstructorService;
import com.project.LMS.service.LessonService;
import com.project.LMS.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/instructor")
public class InstructorController {

    @Autowired
    private final InstructorService instructorService;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private final NotificationService notificationService;

    @Autowired
    private final CourseDao courseDao;
    @Autowired
    private AssignmentSubmissionService assignmentSubmissionService;
    @Autowired
    private AssignmentDao assignmentDao;

    public InstructorController(InstructorService instructorService, NotificationService notificationService, CourseDao courseDao) {
        this.instructorService = instructorService;
        this.notificationService = notificationService;
        this.courseDao = courseDao;
    }

    @DeleteMapping("/deleteStudent/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable int id) {
        try {
            instructorService.deleteStudent(id);
            return ResponseEntity.status(HttpStatus.OK).body("Student deleted successfully.");
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error occurred while deleting student: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }

    @DeleteMapping("/deleteCourse/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable int id) {
        try {
            instructorService.deleteCourse(id);
            return ResponseEntity.status(HttpStatus.OK).body("Course deleted successfully.");
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error occurred while deleting course: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }

    @PostMapping("/addAssignment")
    public ResponseEntity<String> addAssignment(@RequestBody @Validated AssignmentRequest request) {
        try {
            instructorService.addAssignment(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("Assignment added successfully.");
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error occurred while adding assignment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }

    @PostMapping("/addCourse")
    public ResponseEntity<String> addCourse(@RequestBody @Validated Course course) {
        try {
            instructorService.addCourse(course);
            return ResponseEntity.status(HttpStatus.CREATED).body("Course added successfully.");
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error occurred while adding course: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }

    @PostMapping("/addQuiz")
    public ResponseEntity<String> addQuiz(@RequestBody @Validated QuizRequest request) {
        try {
            instructorService.addQuiz(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("Quiz added successfully.");
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error occurred while adding quiz: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }

    @PostMapping("/gradeAssignment")
    public ResponseEntity<String> gradeAssignment(@RequestBody @Validated AssignmentGradeRequest request) {
        try {
            instructorService.gradeAssignment(request);

            String assignmentName = assignmentDao.getAssignmentName(request.getAssignmentId());
            notificationService.sendNotificationToUser(request.getStudentId(),"Assignment: " + assignmentName +  "graded successfully.", "STUDENT");

            return ResponseEntity.status(HttpStatus.OK).body("Assignment graded successfully.");
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error occurred while grading assignment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }

    }

    @PostMapping("/gradeQuiz")
    public ResponseEntity<String> gradeQuiz(@RequestBody @Validated QuizGradeRequest request) {
        try {
            instructorService.gradeQuiz(request);
            return ResponseEntity.status(HttpStatus.OK).body("Quiz graded successfully.");
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error occurred while grading quiz: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }

    @GetMapping("/quizGrades")
    public ResponseEntity<String> getQuizGrades(@RequestParam int quizId) {
        try {
            List<QuizGradeGetRequest> grades = instructorService.listOfGrades(quizId);
            return ResponseEntity.ok(grades.toString());
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error occurred while fetching quiz grades: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }

    @GetMapping("/assignmentSubmissions")
    public ResponseEntity<String> getAssignmentSubmissions(@RequestParam int assignmentId) {
        try {
            List<AssignmentSubmissionsResponse> assignmentSubmissions = instructorService.listOfAssignmentSubmissions(assignmentId);
            return ResponseEntity.ok(assignmentSubmissions.toString());
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error occurred while fetching assignment submissions: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }

    @PostMapping("/confirmEnrollment")
    public String confirmEnrollment(@RequestParam int courseId, @RequestParam int studentId) {
        try {
            String crsName = courseDao.getCourseName(courseId);
            notificationService.sendNotificationToUser(studentId, "You have been approved to enroll in course: " + crsName, "STUDENT");
            return "Confirmation for course: " + crsName + "done successfully.";
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error occurred while enrolling student: " + e.getMessage());
            return errorResponse.toString();
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/EnrolledStudents")
    public ResponseEntity<String> getEnrolledStudents(@RequestParam Integer courseId) {
        return instructorService.getEnrolledStudents(courseId);
    }

    @PostMapping("/generateOtp")
    public ResponseEntity<String> generateOtp(@RequestParam Integer lessonId) {
        return instructorService.generateOtp(lessonId);
    }
}
