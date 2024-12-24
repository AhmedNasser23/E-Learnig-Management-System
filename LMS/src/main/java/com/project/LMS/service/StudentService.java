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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final GradeRepository gradeRepository;
    private final MaterialRepository materialRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, CourseRepository courseRepository, GradeRepository gradeRepository, MaterialRepository materialRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.gradeRepository = gradeRepository;
        this.materialRepository = materialRepository;
    }

    public void enrollInCourse(int studentId, int courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!student.getCourses().contains(course)) {
            student.getCourses().add(course);
            course.getStudents().add(student);
            studentRepository.save(student);
        } else {
            throw new RuntimeException("Student is already enrolled in this course");
        }


    }
    public List<AssignmentsGradesDto> getAssignmentGradesByStudentId(int studentId) {
        return gradeRepository.findAssignmentGradesByStudentId(studentId);
    }
    public List<QuizGradeDto> getQuizGradesByStudentId(int studentId) {
        return gradeRepository.findQuizGradesByStudentId(studentId);
    }
    public List<MaterialDto> getMaterialsByCourseId(Integer courseId) {
        return materialRepository.findMaterialsByCourseId(courseId);
    }

}
