package com.project.LMS.controller;

import com.project.LMS.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
public class LessonController {
    @Autowired
    private LessonService lessonService;
    @PostMapping("/createLesson")
    public ResponseEntity<String> createLesson(@RequestParam("files") List<MultipartFile> files, @RequestParam String title, @RequestParam Integer courseId) {
        return lessonService.createLesson(files, title, courseId);
    }

    @DeleteMapping ("/deleteLesson")
    public ResponseEntity<String> deleteLesson (@RequestParam Integer lessonId) {
        return lessonService.deleteLesson(lessonId);
    }

    @PutMapping("/updateLessonTitle")
    public ResponseEntity<String> updateLessonTitle(@RequestParam Integer lessonId, @RequestParam String newTitle) {
        return lessonService.updateLessonTitle(lessonId, newTitle);
    }

    @PutMapping("/updateLessonByAddFile")
    public ResponseEntity<String> updateLessonByAddFile (@RequestParam MultipartFile file , @RequestParam Integer lessonId) {
        return lessonService.updateLessonByAddFile(file, lessonId);
    }

    @PutMapping("/updateLessonByRemoveFile")
    public ResponseEntity<String> updateLessonByRemoveFile (@RequestParam Integer lessonId, @RequestParam String fileName) {
        return lessonService.updateLessonByRemoveFile(lessonId, fileName);
    }
}
