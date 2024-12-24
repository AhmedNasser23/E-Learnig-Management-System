package com.project.LMS.service;

import com.project.LMS.dao.Assignments.AssignmentSubmissionDao;
import com.project.LMS.dto.assignment.AssignmentSubmissionRequestBody;
import com.project.LMS.entity.Assignment;
import com.project.LMS.entity.users.Student;
import com.project.LMS.repository.AssignmentRepository;
import com.project.LMS.repository.StudentRepository;
import org.springframework.stereotype.Service;

@Service
public class AssignmentSubmissionService {

    private final AssignmentSubmissionDao assignmentSubmissionDao;
    private final AssignmentRepository assignmentRepository;
    private final StudentRepository studentRepository;

    public AssignmentSubmissionService(
            AssignmentSubmissionDao assignmentSubmissionDao,
            AssignmentRepository assignmentRepository,
            StudentRepository studentRepository
    ) {
        this.assignmentSubmissionDao = assignmentSubmissionDao;
        this.assignmentRepository = assignmentRepository;
        this.studentRepository = studentRepository;
    }

    public void submitAssignment(AssignmentSubmissionRequestBody body) {
        int studentId = body.getStudentId();
        int assignmentId = body.getAssignmentId();

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student ID"));

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid assignment ID"));

        if (!assignment.getCourse().getStudents().contains(student)) {
            throw new IllegalStateException("Student is not enrolled in the course for this assignment");
        }

        assignmentSubmissionDao.submitAssignment(body);

    }
}
