package com.project.LMS.service;

import com.project.LMS.dao.Assignments.AssignmentDao;
import com.project.LMS.dao.Assignments.AssignmentGradeDao;
import com.project.LMS.dto.assignment.AssignmentGradeRequest;
import com.project.LMS.dto.assignment.AssignmentRequest;
import com.project.LMS.dao.Course.CourseDao;
import com.project.LMS.dao.Quiz.QuizDao;
import com.project.LMS.dao.Quiz.QuizGradeDao;
import com.project.LMS.dto.assignment.AssignmentSubmissionsResponse;
import com.project.LMS.dto.quiz.QuizGradeGetRequest;
import com.project.LMS.dto.quiz.QuizGradeRequest;
import com.project.LMS.dto.quiz.QuizRequest;
import com.project.LMS.dao.Users.StudentDao;
import com.project.LMS.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.LMS.entity.Enrollment;
import com.project.LMS.entity.Lesson;
import com.project.LMS.entity.users.Student;
import com.project.LMS.repository.CourseRepository;
import com.project.LMS.repository.LessonRepository;
import com.project.LMS.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

@Service
public class InstructorService {
    @Autowired
    private final StudentDao studentDao;
    private final CourseDao courseDao;
    private final AssignmentDao assignmentDao;
    private final QuizDao quizDao;
    private final AssignmentGradeDao assignmentGradeDao;
    private final QuizGradeDao quizGradeDao;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private StudentRepository studentRepository;
//    @Autowired
//    private MailService mailService;
    @Autowired
    private NotificationService notificationService;


    @Autowired
    public InstructorService(StudentDao studentRepository, CourseDao courseRepository, AssignmentDao assignmentRepository, QuizDao quizDao, AssignmentGradeDao assignmentGradeDao, QuizGradeDao quizGradeDao) {
        this.studentDao = studentRepository;
        this.courseDao = courseRepository;
        this.assignmentDao = assignmentRepository;
        this.quizDao = quizDao;
        this.assignmentGradeDao = assignmentGradeDao;
        this.quizGradeDao = quizGradeDao;
    }

    public void addCourse(Course course) {
        courseDao.addCourse(course);
    }
    public void addAssignment(AssignmentRequest request) {
        assignmentDao.addAssigment(request);
    }
    public void addQuiz(QuizRequest request) {
        quizDao.addQuiz(request);
    }

    public void gradeAssignment(AssignmentGradeRequest request) {
        assignmentGradeDao.grade(request);
    }
    public void gradeQuiz(QuizGradeRequest request) {
        quizGradeDao.grade(request);
    }

    public void deleteCourse(int id) {
        courseDao.deleteCourse(id);
    }
    public void deleteStudent(int id) {
        studentDao.deleteStudent(id);
    }

    public List<QuizGradeGetRequest> listOfGrades(int id){
        return quizGradeDao.getGradesByQuizId(id);
    }
    public List<AssignmentSubmissionsResponse> listOfAssignmentSubmissions(int id){
        return assignmentDao.getAssignmentSubmissions(id);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public ResponseEntity<String> getEnrolledStudents(Integer courseId) {
        if (courseRepository.findById(courseId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("invalid course id");
        }
        List<Enrollment> enrollments = courseRepository.findById(courseId).get().getEnrollments();
        String ans = "";
        for (Enrollment e : enrollments) {
            ans += "Student ID: " + e.getStudent().getId() + "\n";
            ans += "Student Name: " + e.getStudent().getName() + "\n";
            ans += "Student Email: " + e.getStudent().getEmail() + "\n";
            ans += "\n";
        }
        return ResponseEntity.ok(ans);
    }
    public ResponseEntity<String> generateOtp(Integer lessonId) {
        Optional<Lesson> optionalLesson = lessonRepository.findById(lessonId);
        if (optionalLesson.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("invalid lesson id");
        }
        SecureRandom random = new SecureRandom();
        String otp = "";
        int otpLength = 6;
        for (int i = 0; i < otpLength; ++i) {
            otp += random.nextInt(10);  // generate random number between 0 - 9
        }
        Lesson lesson = optionalLesson.get();
        lesson.setOtp(otp);
        lesson.setOtpExpiration(LocalDateTime.now().plusMinutes(60)); // OTP valid for 60 minutes
        lessonRepository.save(lesson);
        // Notify students via email
        notifyStudents(lesson, otp);
        return ResponseEntity.ok("OTP generated: " + otp);
    }
    private void notifyStudents(Lesson lesson, String otp) {
        Course course = lesson.getCourse();
        List<Enrollment> enrollments = course.getEnrollments();
//        String subject = "Course: " + course.getTitle() + "\n";
//        subject += "Course ID: " + course.getId() + "\n";
//        subject += "OTP for Lesson: " + lesson.getTitle() + "\n";
//        subject += "Lesson ID: " + lesson.getId();
        String text = "Your OTP for the lesson \"" + lesson.getTitle() + "\nID: " + lesson.getId() + "\" is: " + otp +
                "\n\nPlease use this OTP to mark your attendance.\n" + "otp expires at " + lesson.getOtpExpiration();
        for (Enrollment e : enrollments) {
            Student student = studentRepository.findById(e.getStudent().getId()).get();
//            emailSenderService.sendEmail(student.getEmail(), subject, text);
            notificationService.sendNotificationToUser(student.getId(), text, "STUDENT");
        }
    }
}