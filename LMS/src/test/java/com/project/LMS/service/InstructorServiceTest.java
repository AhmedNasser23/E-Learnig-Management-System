package com.project.LMS.service;

import com.project.LMS.dao.Assignments.AssignmentDao;
import com.project.LMS.dao.Assignments.AssignmentGradeDao;
import com.project.LMS.dao.Course.CourseDao;
import com.project.LMS.dao.Quiz.QuizDao;
import com.project.LMS.dao.Quiz.QuizGradeDao;
import com.project.LMS.dao.Users.StudentDao;
import com.project.LMS.dto.assignment.AssignmentGradeRequest;
import com.project.LMS.dto.assignment.AssignmentRequest;
import com.project.LMS.dto.quiz.QuizGradeRequest;
import com.project.LMS.dto.quiz.QuizRequest;
import com.project.LMS.entity.Course;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstructorServiceTest {

    @Mock
    private StudentDao studentDao;

    @Mock
    private CourseDao courseDao;

    @Mock
    private AssignmentDao assignmentDao;

    @Mock
    private QuizDao quizDao;

    @Mock
    private AssignmentGradeDao assignmentGradeDao;

    @Mock
    private QuizGradeDao quizGradeDao;

    @InjectMocks
    private InstructorService instructorService;

    @Test
    void testAddCourse() {
        Course course = new Course();
        course.setId(1);
        course.setTitle("Math");

        instructorService.addCourse(course);

        verify(courseDao, times(1)).addCourse(course);
    }

    @Test
    void testAddAssignment() {
        AssignmentRequest request = new AssignmentRequest();
        request.setTitle("Homework 1");

        instructorService.addAssignment(request);

        verify(assignmentDao, times(1)).addAssigment(request);
    }

    @Test
    void testAddQuiz() {
        QuizRequest request = new QuizRequest();
        request.setTitle("Quiz 1");

        instructorService.addQuiz(request);

        verify(quizDao, times(1)).addQuiz(request);
    }

    @Test
    void testGradeAssignment() {
        AssignmentGradeRequest request = new AssignmentGradeRequest();
        request.setStudentId(1);
        request.setAssignmentId(101);
        request.setScore(95);

        instructorService.gradeAssignment(request);

        verify(assignmentGradeDao, times(1)).grade(request);
    }

    @Test
    void testGradeQuiz() {
        QuizGradeRequest request = new QuizGradeRequest();
        request.setStudentId(1);
        request.setQuizId(201);
        request.setScore(85);

        instructorService.gradeQuiz(request);

        verify(quizGradeDao, times(1)).grade(request);
    }

    @Test
    void testDeleteCourse() {
        int courseId = 1;

        instructorService.deleteCourse(courseId);

        verify(courseDao, times(1)).deleteCourse(courseId);
    }

    @Test
    void testDeleteStudent() {
        int studentId = 1;

        instructorService.deleteStudent(studentId);

        verify(studentDao, times(1)).deleteStudent(studentId);
    }
}
