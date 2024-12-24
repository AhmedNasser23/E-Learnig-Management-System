package com.project.LMS.controller;

import com.project.LMS.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/recordAttendance")
    public ResponseEntity<String> recordAttendance (@RequestParam Integer studentId, @RequestParam Integer lessonId, String otp) {
        return attendanceService.recordAttendance(studentId, lessonId, otp);
    }
}