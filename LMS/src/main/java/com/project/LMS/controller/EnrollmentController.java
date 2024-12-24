package com.project.LMS.controller;

import com.project.LMS.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnrollmentController {
    @Autowired
    private EnrollmentService enrollmentService;

//    @PostMapping("/enroll")
//    public ResponseEntity<String> enroll(@RequestParam Integer studentId, @RequestParam Integer courseId) {
//        return enrollmentService.enroll(studentId, courseId);
//    }

    @DeleteMapping("/unenroll")
    public ResponseEntity<String> unenroll (@RequestParam Integer studentId, @RequestParam Integer courseId) {
        return enrollmentService.unenroll(studentId, courseId);
    }
}
