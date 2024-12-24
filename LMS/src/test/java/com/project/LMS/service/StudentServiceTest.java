package com.project.LMS.service;

import com.project.LMS.dto.assignment.AssignmentsGradesDto;
import com.project.LMS.dto.material.MaterialDto;
import com.project.LMS.dto.quiz.QuizGradeDto;
import com.project.LMS.entity.Course;
import com.project.LMS.entity.users.Student;
import com.project.LMS.repository.CourseRepository;
import com.project.LMS.repository.GradeRepository;
import com.project.LMS.repository.MaterialRepository;
import com.project.LMS.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private GradeRepository gradeRepository;

    @Mock
    private MaterialRepository materialRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void testEnrollInCourse_Success() {
        int studentId = 1;
        int courseId = 101;

        Student student = new Student();
        student.setId(studentId);
        student.setCourses(new ArrayList<>());

        Course course = new Course();
        course.setId(courseId);
        course.setStudents(new ArrayList<>());

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        studentService.enrollInCourse(studentId, courseId);

        assertTrue(student.getCourses().contains(course));
        assertTrue(course.getStudents().contains(student));
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void testEnrollInCourse_StudentNotFound() {
        int studentId = 1;
        int courseId = 101;

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            studentService.enrollInCourse(studentId, courseId);
        });

        assertEquals("Student not found", exception.getMessage());
        verify(studentRepository, times(1)).findById(studentId);
        verifyNoMoreInteractions(courseRepository, studentRepository);
    }

    @Test
    void testEnrollInCourse_CourseNotFound() {
        int studentId = 1;
        int courseId = 101;

        Student student = new Student();
        student.setId(studentId);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            studentService.enrollInCourse(studentId, courseId);
        });

        assertEquals("Course not found", exception.getMessage());
        verify(studentRepository, times(1)).findById(studentId);
        verify(courseRepository, times(1)).findById(courseId);
    }

    @Test
    void testEnrollInCourse_AlreadyEnrolled() {
        int studentId = 1;
        int courseId = 101;

        Course course = new Course();
        course.setId(courseId);

        Student student = new Student();
        student.setId(studentId);
        student.setCourses(Arrays.asList(course));

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            studentService.enrollInCourse(studentId, courseId);
        });

        assertEquals("Student is already enrolled in this course", exception.getMessage());
        verify(studentRepository, times(1)).findById(studentId);
        verify(courseRepository, times(1)).findById(courseId);
    }

    @Test
    void testGetAssignmentGradesByStudentId() {
        int studentId = 1;

        AssignmentsGradesDto grade1 = new AssignmentsGradesDto();
        grade1.setAssignmentName("Assignment 1");
        grade1.setAssignmentGrade(85.0);

        AssignmentsGradesDto grade2 = new AssignmentsGradesDto();
        grade2.setAssignmentName("Assignment 2");
        grade2.setAssignmentGrade(90.0);

        List<AssignmentsGradesDto> grades = Arrays.asList(grade1, grade2);

        when(gradeRepository.findAssignmentGradesByStudentId(studentId)).thenReturn(grades);

        List<AssignmentsGradesDto> result = studentService.getAssignmentGradesByStudentId(studentId);

        assertEquals(2, result.size());
        assertEquals("Assignment 1", result.get(0).getAssignmentName());
        assertEquals(85, result.get(0).getAssignmentGrade());
        verify(gradeRepository, times(1)).findAssignmentGradesByStudentId(studentId);
    }

    @Test
    void testGetQuizGradesByStudentId() {
        int studentId = 1;

        QuizGradeDto grade1 = new QuizGradeDto();
        grade1.setQuizName("Quiz 1");
        grade1.setQuizGrade(75.0);

        QuizGradeDto grade2 = new QuizGradeDto();
        grade2.setQuizName("Quiz 2");
        grade2.setQuizGrade(88.0);

        List<QuizGradeDto> grades = Arrays.asList(grade1, grade2);

        when(gradeRepository.findQuizGradesByStudentId(studentId)).thenReturn(grades);

        List<QuizGradeDto> result = studentService.getQuizGradesByStudentId(studentId);

        assertEquals(2, result.size());
        assertEquals("Quiz 1", result.get(0).getQuizName());
        assertEquals(75, result.get(0).getQuizGrade());
        verify(gradeRepository, times(1)).findQuizGradesByStudentId(studentId);
    }

    @Test
    void testGetMaterialsByCourseId() {
        int courseId = 101;

        MaterialDto material1 = new MaterialDto();
        material1.setTitle("Chapter 1");

        MaterialDto material2 = new MaterialDto();
        material2.setTitle("Chapter 2");

        List<MaterialDto> materials = Arrays.asList(material1, material2);

        when(materialRepository.findMaterialsByCourseId(courseId)).thenReturn(materials);

        List<MaterialDto> result = studentService.getMaterialsByCourseId(courseId);

        assertEquals(2, result.size());
        assertEquals("Chapter 1", result.get(0).getTitle());
        verify(materialRepository, times(1)).findMaterialsByCourseId(courseId);
    }
}
