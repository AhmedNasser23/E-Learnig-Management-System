package com.project.LMS.controller;

import com.project.LMS.entity.users.Admin;
import com.project.LMS.entity.Course;
import com.project.LMS.entity.users.Instructor;
import com.project.LMS.entity.users.Student;
import com.project.LMS.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/addInstructor")
    public ResponseEntity<String> addInstructor(@RequestBody @Validated Instructor instructor) {
        try {
            adminService.addInstructor(instructor);
            return ResponseEntity.status(HttpStatus.CREATED).body("Instructor added successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while adding instructor: " + e.getMessage());
        }
    }

    @PostMapping("/addAdmin")
    public ResponseEntity<String> addAdmin(@RequestBody @Validated Admin admin) {
        try {
            adminService.addAdmin(admin);
            return ResponseEntity.status(HttpStatus.CREATED).body("Admin added successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while adding admin: " + e.getMessage());
        }
    }

    @PostMapping("/addStudent")
    public ResponseEntity<String> addStudent(@RequestBody @Validated Student student) {
        try {
            adminService.addStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED).body("Student added successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while adding student: " + e.getMessage());
        }
    }

    @PostMapping("/addCourse")
    public ResponseEntity<String> addCourse(@RequestBody @Validated Course course) {
        try {
            adminService.addCourse(course);
            return ResponseEntity.status(HttpStatus.CREATED).body("Course added successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while adding course: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteStudent/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable int id) {
        try {
            adminService.deleteStudent(id);
            return ResponseEntity.status(HttpStatus.OK).body("Student deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while deleting student: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteInstructor/{id}")
    public ResponseEntity<String> deleteInstructor(@PathVariable int id) {
        try {
            adminService.deleteInstructor(id);
            return ResponseEntity.status(HttpStatus.OK).body("Instructor deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while deleting instructor: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteAdmin/{id}")
    public ResponseEntity<String> deleteAdmin(@PathVariable int id) {
        try {
            adminService.deleteAdmin(id);
            return ResponseEntity.status(HttpStatus.OK).body("Admin deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while deleting admin: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteCourse/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable int id) {
        try {
            adminService.deleteCourse(id);
            return ResponseEntity.status(HttpStatus.OK).body("Course deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while deleting course: " + e.getMessage());
        }
    }
}
