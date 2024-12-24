package com.project.LMS.controller;

import com.project.LMS.dto.quiz.QuizSubmissionResponse;
import com.project.LMS.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/submit/{quizId}/{studentId}")
    public ResponseEntity<?> submitQuizAttempt(
            @PathVariable int quizId,
            @PathVariable int studentId,
            @RequestBody @Validated Map<Integer, String> answers
    ) {
        try {
            QuizSubmissionResponse response = quizService.submitQuizAttempt(quizId, studentId, answers);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while submitting quiz: " + e.getMessage());
        }
    }
}
