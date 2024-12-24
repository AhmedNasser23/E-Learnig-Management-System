package com.project.LMS.service;

import com.project.LMS.dao.Assignments.AssignmentSubmissionDao;
import com.project.LMS.dto.assignment.AssignmentSubmissionRequestBody;
import com.project.LMS.entity.Assignment;
import com.project.LMS.entity.Course;
import com.project.LMS.entity.users.Student;
import com.project.LMS.repository.AssignmentRepository;
import com.project.LMS.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssignmentSubmissionServiceTest {

    @Mock
    private AssignmentSubmissionDao assignmentSubmissionDao;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private AssignmentSubmissionService assignmentSubmissionService;

    @Test
    void testSubmitAssignment_Success() {
        int studentId = 1;
        int assignmentId = 101;
        String fileUrl = "http://example.com/file.pdf";

        AssignmentSubmissionRequestBody requestBody = new AssignmentSubmissionRequestBody();
        requestBody.setStudentId(studentId);
        requestBody.setAssignmentId(assignmentId);
        requestBody.setFileURL(fileUrl);

        Student student = new Student();
        student.setId(studentId);

        Assignment assignment = new Assignment();
        assignment.setId(assignmentId);

        Course course = new Course();
        course.setId(1);
        course.getStudents().add(student);
        assignment.setCourse(course);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(assignment));

        assignmentSubmissionService.submitAssignment(requestBody);

        verify(studentRepository, times(1)).findById(studentId);
        verify(assignmentRepository, times(1)).findById(assignmentId);
        verify(assignmentSubmissionDao, times(1)).submitAssignment(requestBody);
    }

    @Test
    void testSubmitAssignment_InvalidStudentId() {
        int studentId = 999;
        int assignmentId = 101;
        AssignmentSubmissionRequestBody requestBody = new AssignmentSubmissionRequestBody();
        requestBody.setStudentId(studentId);
        requestBody.setAssignmentId(assignmentId);

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            assignmentSubmissionService.submitAssignment(requestBody);
        });

        assertEquals("Invalid student ID", exception.getMessage());
        verify(studentRepository, times(1)).findById(studentId);
        verifyNoInteractions(assignmentRepository, assignmentSubmissionDao);
    }

    @Test
    void testSubmitAssignment_InvalidAssignmentId() {
        int studentId = 1;
        int assignmentId = 999;
        AssignmentSubmissionRequestBody requestBody = new AssignmentSubmissionRequestBody();
        requestBody.setStudentId(studentId);
        requestBody.setAssignmentId(assignmentId);

        Student student = new Student();
        student.setId(studentId);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            assignmentSubmissionService.submitAssignment(requestBody);
        });

        assertEquals("Invalid assignment ID", exception.getMessage());
        verify(studentRepository, times(1)).findById(studentId);
        verify(assignmentRepository, times(1)).findById(assignmentId);
        verifyNoInteractions(assignmentSubmissionDao);
    }

    @Test
    void testSubmitAssignment_StudentNotEnrolledInCourse() {
        int studentId = 1;
        int assignmentId = 101;
        AssignmentSubmissionRequestBody requestBody = new AssignmentSubmissionRequestBody();
        requestBody.setStudentId(studentId);
        requestBody.setAssignmentId(assignmentId);

        Student student = new Student();
        student.setId(studentId);

        Assignment assignment = new Assignment();
        assignment.setId(assignmentId);

        Course course = new Course();
        course.setId(1);
        assignment.setCourse(course);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(assignment));

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            assignmentSubmissionService.submitAssignment(requestBody);
        });

        assertEquals("Student is not enrolled in the course for this assignment", exception.getMessage());
        verify(studentRepository, times(1)).findById(studentId);
        verify(assignmentRepository, times(1)).findById(assignmentId);
        verifyNoInteractions(assignmentSubmissionDao);
    }
}
