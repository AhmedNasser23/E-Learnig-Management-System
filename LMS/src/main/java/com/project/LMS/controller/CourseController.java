package com.project.LMS.controller;

import com.project.LMS.entity.Course;
import com.project.LMS.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CourseController {
    @Autowired
    private CourseService courseService;

//    @PostMapping ("/createCourse")
//    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
//        Course createdCourse = courseService.createCourse(course);
//        return ResponseEntity.ok(createdCourse);
//    }

//    @DeleteMapping("/deleteCourse")
//    public ResponseEntity<String> deleteCourse(@RequestParam Integer id) {
//        return courseService.deleteCourse(id);
//    }

    @PutMapping ("/updateCourseTitle")
    public ResponseEntity<String> updateCourseTitle (@RequestParam Integer id, @RequestParam String title) {
        Course updatedCourse = courseService.updateCourseTitle(id, title);
        if (updatedCourse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course with ID " + id + " does not exist");
        }
        return ResponseEntity.ok("Course with ID " + id + " has been updated\n" + updatedCourse.details());
    }

    @PutMapping ("/updateCourseDescription")
    public ResponseEntity<String> updateCourseDescription (@RequestParam Integer id, @RequestParam String description) {
        Course updatedCourse = courseService.updateCourseDescription(id, description);
        if (updatedCourse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course with ID " + id + " does not exist");
        }
        return ResponseEntity.ok("Course with ID " + id + " has been updated\n" + updatedCourse.details());
    }

    @PutMapping ("/updateCourseDuration")
    public ResponseEntity<String> updateCourseDuration (@RequestParam Integer id, @RequestParam Double duration) {
        Course updatedCourse = courseService.updateCourseDuration(id, duration);
        if (updatedCourse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course with ID " + id + " does not exist");
        }
        return ResponseEntity.ok("Course with ID " + id + " has been updated\n" + updatedCourse.details());
    }

    @GetMapping("/getAllCourses")
    public String getAllCourses () {
        return courseService.getAllCourses();
    }

    @GetMapping("/getCourseLessons")
    public ResponseEntity<String> getCourseLessons (@RequestParam Integer courseId) {
        return courseService.getCourseLessons(courseId);
    }

    @GetMapping("/displayFile")
    public ResponseEntity displayFile(@RequestParam String fileName, @RequestParam Integer courseId, @RequestParam Integer lessonId, @RequestParam Integer studentId) {
        return courseService.displayFile(fileName, courseId, lessonId, studentId);
    }

}