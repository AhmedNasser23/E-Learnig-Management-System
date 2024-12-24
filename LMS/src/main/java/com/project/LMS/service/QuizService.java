package com.project.LMS.service;

import com.project.LMS.dto.quiz.AnswerBody;
import com.project.LMS.dao.Quiz.QuizDao;
import com.project.LMS.dto.quiz.QuizSubmissionResponse;
import com.project.LMS.entity.Question;
import com.project.LMS.entity.Quiz;
import com.project.LMS.entity.QuizAttempt;
import com.project.LMS.entity.users.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class QuizService {

    @Autowired
    private QuizDao quizDao;

    public QuizSubmissionResponse submitQuizAttempt(int quizId, int studentId, Map<Integer, String> answers) {

        Quiz quiz = quizDao.getQuizById(quizId);
        if (quiz == null) {
            throw new IllegalArgumentException("Quiz not found for ID: " + quizId);
        }

        Student student = new Student();
        student.setId(studentId);

        QuizAttempt attempt = new QuizAttempt();
        attempt.setQuiz(quiz);
        attempt.setStudent(student);
        attempt.setAttemptDate(LocalDateTime.now());

        QuizAttempt savedAttempt = quizDao.saveQuizAttempt(attempt);
        Integer attempt_id = attempt.getId();

        double totalQuestions = answers.size();
        double correctAnswers = 0;

        for (Map.Entry<Integer, String> entry : answers.entrySet()) {
            Integer questionId = entry.getKey();
            String studentAnswer = entry.getValue();

            Question question = quizDao.getQuestionById(questionId);

            AnswerBody answer = new AnswerBody();
            answer.setAttempt_id(attempt_id);
            answer.setQuestion_id(questionId);
            answer.setStudentAnswer(studentAnswer);
            quizDao.saveAnswer(answer);

            if (studentAnswer.equalsIgnoreCase(question.getCorrectAnswer())) {
                correctAnswers++;
            }
        }

        double score = (correctAnswers / totalQuestions) * 100;
        savedAttempt.setScore(score);
        quizDao.saveQuizAttempt(savedAttempt);

        return new QuizSubmissionResponse(quiz.getTitle(), score);
    }
}
