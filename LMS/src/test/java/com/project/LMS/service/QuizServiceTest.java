package com.project.LMS.service;

import com.project.LMS.dao.Quiz.QuizDao;
import com.project.LMS.dto.quiz.AnswerBody;
import com.project.LMS.dto.quiz.QuizSubmissionResponse;
import com.project.LMS.entity.Question;
import com.project.LMS.entity.Quiz;
import com.project.LMS.entity.QuizAttempt;
import com.project.LMS.entity.users.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizServiceTest {

    @Mock
    private QuizDao quizDao;

    @InjectMocks
    private QuizService quizService;

    @Test
    void testSubmitQuizAttempt_Success() {
        int quizId = 101;
        int studentId = 1;

        Quiz quiz = new Quiz();
        quiz.setId(quizId);
        quiz.setTitle("Java Basics Quiz");

        Question question1 = new Question();
        question1.setId(1);
        question1.setCorrectAnswer("A");

        Question question2 = new Question();
        question2.setId(2);
        question2.setCorrectAnswer("B");

        Map<Integer, String> answers = new HashMap<>();
        answers.put(1, "A");
        answers.put(2, "C");

        QuizAttempt attempt = new QuizAttempt();
        attempt.setId(1001);
        attempt.setQuiz(quiz);
        attempt.setStudent(new Student());
        attempt.setAttemptDate(LocalDateTime.now());

        when(quizDao.getQuizById(quizId)).thenReturn(quiz);
        when(quizDao.getQuestionById(1)).thenReturn(question1);
        when(quizDao.getQuestionById(2)).thenReturn(question2);
        when(quizDao.saveQuizAttempt(any(QuizAttempt.class))).thenReturn(attempt);

        QuizSubmissionResponse response = quizService.submitQuizAttempt(quizId, studentId, answers);

        assertNotNull(response);
        assertEquals("Java Basics Quiz", response.getQuizName());
        assertEquals(50.0, response.getScore(), 0.01);

        verify(quizDao, times(1)).getQuizById(quizId);
        verify(quizDao, times(1)).getQuestionById(1);
        verify(quizDao, times(1)).getQuestionById(2);
        verify(quizDao, times(2)).saveAnswer(any(AnswerBody.class));
        verify(quizDao, times(2)).saveQuizAttempt(any(QuizAttempt.class));
    }

    @Test
    void testSubmitQuizAttempt_QuizNotFound() {
        int quizId = 999;
        int studentId = 1;
        Map<Integer, String> answers = new HashMap<>();

        when(quizDao.getQuizById(quizId)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            quizService.submitQuizAttempt(quizId, studentId, answers);
        });

        assertEquals("Quiz not found for ID: 999", exception.getMessage());
        verify(quizDao, times(1)).getQuizById(quizId);
        verifyNoMoreInteractions(quizDao);
    }

    @Test
    void testSubmitQuizAttempt_CorrectAnswerCalculation() {
        int quizId = 101;
        int studentId = 1;

        Quiz quiz = new Quiz();
        quiz.setId(quizId);
        quiz.setTitle("Java Basics Quiz");

        Question question1 = new Question();
        question1.setId(1);
        question1.setCorrectAnswer("A");

        Question question2 = new Question();
        question2.setId(2);
        question2.setCorrectAnswer("B");

        Map<Integer, String> answers = new HashMap<>();
        answers.put(1, "A");
        answers.put(2, "B");

        QuizAttempt attempt = new QuizAttempt();
        attempt.setId(1001);

        when(quizDao.getQuizById(quizId)).thenReturn(quiz);
        when(quizDao.getQuestionById(1)).thenReturn(question1);
        when(quizDao.getQuestionById(2)).thenReturn(question2);
        when(quizDao.saveQuizAttempt(any(QuizAttempt.class))).thenReturn(attempt);

        QuizSubmissionResponse response = quizService.submitQuizAttempt(quizId, studentId, answers);

        assertEquals(100.0, response.getScore(), 0.01);

        verify(quizDao, times(2)).getQuestionById(anyInt());
    }
}
