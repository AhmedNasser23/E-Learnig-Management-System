package com.project.LMS.dao.Quiz;

import com.project.LMS.dto.quiz.QuizGradeGetRequest;
import com.project.LMS.dto.quiz.QuizGradeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QuizGradeDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_GRADE_FOR_QUIZZ = "INSERT INTO Grades (id, quiz_id, student_id, score) VALUES(?, ?, ?, ?)";
    private static final String GET_GRADES_BY_QUIZ_ID =
            "SELECT s.name AS student_name, g.score AS student_grade " +
                    "FROM Grades g " +
                    "JOIN Students s ON g.student_id = s.id " +
                    "WHERE g.quiz_id = ?";

    public void grade(QuizGradeRequest request) {
        jdbcTemplate.update(
                INSERT_GRADE_FOR_QUIZZ,
                request.getGradeId(),
                request.getQuizId(),
                request.getStudentId(),
                request.getScore()
        );
    }

    public List<QuizGradeGetRequest> getGradesByQuizId(int quizId) {
        return jdbcTemplate.query(GET_GRADES_BY_QUIZ_ID, new Object[]{quizId}, studentGradeMapper);
    }

    private final RowMapper<QuizGradeGetRequest> studentGradeMapper = (rs, rowNum) -> {
        QuizGradeGetRequest response = new QuizGradeGetRequest();
        response.setStudentName(rs.getString("student_name"));
        response.setQuizGrade(rs.getDouble("student_grade"));
        return response;
    };


}
