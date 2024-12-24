package com.project.LMS.dao.Quiz;

import com.project.LMS.dto.quiz.AnswerBody;
import com.project.LMS.dto.quiz.QuizRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.project.LMS.entity.Question;
import com.project.LMS.entity.Quiz;
import com.project.LMS.entity.QuizAttempt;

import java.sql.Timestamp;

@Repository
public class QuizDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SELECT_QUESTION_BY_ID_SQL = "SELECT id, questionText, correctAnswer FROM Questions WHERE id = ?";
    private static final String SELECT_QUIZ_BY_ID_SQL = "SELECT id, title, course_id FROM Quizzes WHERE id = ?";
    private static final String INSERT_QUIZ_SQL = "INSERT INTO Quizzes (id, date, title, course_id) VALUES (?, ?, ?, ?)";

    public void addQuiz(QuizRequest quizRequest) {
        jdbcTemplate.update(INSERT_QUIZ_SQL, quizRequest.getId(), quizRequest.getDate(), quizRequest.getTitle(), quizRequest.getCourseId());
    }
    public Quiz getQuizById(int quizId) {
        return jdbcTemplate.queryForObject(SELECT_QUIZ_BY_ID_SQL, new Object[]{quizId}, (rs, rowNum) -> {
            Quiz quiz = new Quiz();
            quiz.setId(rs.getInt("id"));
            quiz.setTitle(rs.getString("title"));
            quiz.setCourseId(rs.getInt("course_id"));
            return quiz;
        });
    }
    public Question getQuestionById(int questionId) {
        return jdbcTemplate.queryForObject(SELECT_QUESTION_BY_ID_SQL, new Object[]{questionId}, (rs, rowNum) -> {
            Question question = new Question();
            question.setId(rs.getInt("id"));
            question.setQuestionText(rs.getString("questionText"));
            question.setCorrectAnswer(rs.getString("correctAnswer"));
            return question;
        });
    }
    public QuizAttempt saveQuizAttempt(QuizAttempt attempt) {
        String insertSql = "INSERT INTO Quiz_Attempts (quiz_id, student_id, attemptDate, score) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(insertSql,
                attempt.getQuiz().getId(),
                attempt.getStudent().getId(),
                Timestamp.valueOf(attempt.getAttemptDate()),
                attempt.getScore()
        );
        return attempt;
    }

    public void saveAnswer(AnswerBody answer) {
        String insertAnswerSql = "INSERT INTO Answers (attempt_id, question_id, studentAnswer) VALUES (?, ?, ?)";
        jdbcTemplate.update(insertAnswerSql, answer.getAttempt_id(), answer.getQuestion_id(), answer.getStudentAnswer());
    }
}
