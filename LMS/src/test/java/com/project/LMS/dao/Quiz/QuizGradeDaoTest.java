package com.project.LMS.dao.Quiz;

import com.project.LMS.dto.quiz.QuizGradeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.Mockito.*;

class QuizGradeDaoTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private QuizGradeDao quizGradeDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGrade() {
        QuizGradeRequest request = new QuizGradeRequest();
        request.setGradeId(1);
        request.setQuizId(101);
        request.setStudentId(123);
        request.setScore(95);

        quizGradeDao.grade(request);

        verify(jdbcTemplate).update(
                eq("INSERT INTO Grades (id, quiz_id, student_id, score) VALUES(?, ?, ?, ?)"),
                eq(1),
                eq(101),
                eq(123),
                eq(95)
        );
    }
}
