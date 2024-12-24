package com.project.LMS.dao.Quiz;

import com.project.LMS.dto.quiz.AnswerBody;
import com.project.LMS.dto.quiz.QuizRequest;
import com.project.LMS.entity.Question;
import com.project.LMS.entity.Quiz;
import com.project.LMS.entity.QuizAttempt;
import com.project.LMS.entity.users.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class QuizDaoTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private QuizDao quizDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddQuiz() {
        QuizRequest quizRequest = new QuizRequest();
        quizRequest.setId(1);
        quizRequest.setDate(LocalDate.parse("2024-01-01"));
        quizRequest.setTitle("Sample Quiz");
        quizRequest.setCourseId(101);

        quizDao.addQuiz(quizRequest);

        verify(jdbcTemplate).update(
                eq("INSERT INTO Quizzes (id, date, title, course_id) VALUES (?, ?, ?, ?)"),
                eq(quizRequest.getId()),
                eq(quizRequest.getDate()),
                eq(quizRequest.getTitle()),
                eq(quizRequest.getCourseId())
        );
    }
    @Test
    public void testGetQuizById() {
        int quizId = 1;
        Quiz expectedQuiz = new Quiz(quizId, "Sample Title", 101);

        when(jdbcTemplate.queryForObject(
                eq("SELECT id, title, course_id FROM Quizzes WHERE id = ?"),
                argThat(args -> args.length == 1 && (int) args[0] == quizId),
                any(RowMapper.class)
        )).thenReturn(expectedQuiz);

        Quiz actualQuiz = quizDao.getQuizById(quizId);

        assertNotNull(actualQuiz);
        assertEquals(expectedQuiz, actualQuiz);

        verify(jdbcTemplate).queryForObject(
                eq("SELECT id, title, course_id FROM Quizzes WHERE id = ?"),
                argThat(args -> args.length == 1 && (int) args[0] == quizId),
                any(RowMapper.class)
        );
    }

    @Test
    public void testGetQuestionById() {
        int questionId = 1;
        Question expectedQuestion = new Question();
        expectedQuestion.setId(questionId);
        expectedQuestion.setQuestionText("What is Java?");
        expectedQuestion.setCorrectAnswer("A programming language");

        when(jdbcTemplate.queryForObject(
                eq("SELECT id, questionText, correctAnswer FROM Questions WHERE id = ?"),
                eq(new Object[]{questionId}),
                any(RowMapper.class)
        )).thenReturn(expectedQuestion);

        Question actualQuestion = quizDao.getQuestionById(questionId);

        assertNotNull(actualQuestion);
        assertEquals(expectedQuestion, actualQuestion);

        verify(jdbcTemplate).queryForObject(
                eq("SELECT id, questionText, correctAnswer FROM Questions WHERE id = ?"),
                eq(new Object[]{questionId}),
                any(RowMapper.class)
        );
    }
    @Test
    public void testSaveQuizAttempt() {
        Quiz quiz = new Quiz();
        quiz.setId(1);

        Student student = new Student();
        student.setId(123);

        QuizAttempt attempt = new QuizAttempt();
        attempt.setQuiz(quiz);
        attempt.setStudent(student);
        attempt.setAttemptDate(LocalDateTime.parse("2024-01-01T00:00:00"));
        attempt.setScore(95.0);

        quizDao.saveQuizAttempt(attempt);

        verify(jdbcTemplate).update(
                eq("INSERT INTO Quiz_Attempts (quiz_id, student_id, attemptDate, score) VALUES (?, ?, ?, ?)"),
                eq(attempt.getQuiz().getId()),
                eq(attempt.getStudent().getId()),
                eq(Timestamp.valueOf(attempt.getAttemptDate())),
                eq(attempt.getScore())
        );
    }
    @Test
    public void testSaveAnswer() {
        AnswerBody answer = new AnswerBody();
        answer.setAttempt_id(1);
        answer.setQuestion_id(101);
        answer.setStudentAnswer("42");

        quizDao.saveAnswer(answer);

        verify(jdbcTemplate).update(
                eq("INSERT INTO Answers (attempt_id, question_id, studentAnswer) VALUES (?, ?, ?)"),
                eq(answer.getAttempt_id()),
                eq(answer.getQuestion_id()),
                eq(answer.getStudentAnswer())
        );
    }
}
