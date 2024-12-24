package com.project.LMS.repository;

import com.project.LMS.dto.assignment.AssignmentsGradesDto;
import com.project.LMS.dto.quiz.QuizGradeDto;
import com.project.LMS.entity.Grade;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends CrudRepository<Grade, Long> {

    @Query("""
        SELECT new com.project.LMS.dto.assignment.AssignmentsGradesDto(
            a.title AS assignmentName,
            g.score AS assignmentGrade
        )
        FROM Grade g
        JOIN g.assignment a
        WHERE g.student.id = :studentId
    """)
    List<AssignmentsGradesDto> findAssignmentGradesByStudentId(int studentId);

    @Query("""
        SELECT new com.project.LMS.dto.quiz.QuizGradeDto(
            q.title AS quizName,
            g.score AS quizGrade
        )
        FROM Grade g
        JOIN g.quiz q
        WHERE g.student.id = :studentId
    """)
    List<QuizGradeDto> findQuizGradesByStudentId(int studentId);
}
