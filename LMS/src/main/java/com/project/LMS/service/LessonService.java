package com.project.LMS.service;
import com.project.LMS.entity.Course;
import com.project.LMS.entity.Lesson;
import com.project.LMS.entity.users.MediaFile;
import com.project.LMS.repository.CourseRepository;
import com.project.LMS.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
@Service
public class LessonService {
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private CourseRepository courseRepository;
    public ResponseEntity<String> createLesson (List<MultipartFile> files, String title, Integer courseId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if (optionalCourse.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("failed to create lesson\ninvalid course id");
        }
        Lesson lesson = new Lesson();
        lesson.setTitle(title);
        Course course = new Course();
        course.setId(courseId);
        lesson.setCourse(course);
        for (MultipartFile file : files) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            lesson.addFile(fileName);
        }
        Lesson createdLesson = lessonRepository.save(lesson);
        String courseDirPath = System.getProperty("user.dir") + "/uploadedFiles" + "/course" + courseId + "files";
        String lessonDirPath = courseDirPath + "/lesson" + createdLesson.getId();
        File directory = new File(lessonDirPath);
        if (directory.mkdirs()) {
            System.out.println("Directory created successfully: " + courseDirPath);
        } else {
            System.out.println("Failed to create directory: " + courseDirPath);
        }
        for (MultipartFile file : files) {
            try {
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                // Save the file to the target location
                Path targetLocation = Paths.get(lessonDirPath).resolve(fileName);
                // upload file if it does not exist in the directory
                if (!Files.exists(targetLocation)) {
                    Files.copy(file.getInputStream(), targetLocation);
                }
            } catch (IOException ex) {
                return ResponseEntity.status(500).body("Error saving file: " + ex.getMessage());
            }
        }
        return ResponseEntity.ok("lesson created successfully with id ( " + createdLesson.getId() + " )");
    }
    public ResponseEntity<String> deleteLesson (Integer lessonId) {
        Optional<Lesson> optionalLesson = lessonRepository.findById(lessonId);
        if (optionalLesson.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("failed to delete the lesson\ninvalid lesson id");
        }
        Integer courseId = optionalLesson.get().getCourse().getId();
        String courseDirPath = System.getProperty("user.dir") + "/uploadedFiles" + "/course" + courseId + "files";
        String lessonDirPath = courseDirPath + "/lesson" + optionalLesson.get().getId();
        List<MediaFile> lessonFiles = optionalLesson.get().getMediaFiles();
        // delete lesson files
        for (MediaFile fileName : lessonFiles) {
            Path filePath = Paths.get(lessonDirPath).resolve(fileName.getFileName());
            try {
                Files.delete(filePath);
            }
            catch (IOException e) {
                System.out.println("An error occurred while deleting the file: " + e.getMessage());
            }
        }
        // delete lesson directory
        Path lessonPath = Paths.get(lessonDirPath);
        try {
            Files.delete(lessonPath);
        }
        catch (IOException e) {
        }
        lessonRepository.deleteById(lessonId);
        return ResponseEntity.ok("lesson with ID " + lessonId + " has been deleted");
    }
    public ResponseEntity<String> updateLessonTitle (Integer lessonId, String newTitle) {
        Optional<Lesson> optionalLesson = lessonRepository.findById(lessonId);
        if (optionalLesson.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("failed to update the lesson\ninvalid lesson id");
        }
        Lesson lesson = optionalLesson.get();
        lesson.setTitle(newTitle);
        lessonRepository.save(lesson);
        return ResponseEntity.ok("lesson with id " + lessonId + " has been updated successfully");
    }
    public ResponseEntity<String> updateLessonByAddFile (MultipartFile file, Integer lessonId) {
        Optional<Lesson> optionalLesson = lessonRepository.findById(lessonId);
        if (optionalLesson.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("failed to update lesson\ninvalid lesson id");
        }
        Lesson lesson = optionalLesson.get();
        Integer courseId = lesson.getCourse().getId();
        String courseDirPath = System.getProperty("user.dir") + "/uploadedFiles" + "/course" + courseId + "files";
        String lessonDirPath = courseDirPath + "/lesson" + lesson.getId();
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        // Save the file to the target location
        Path targetLocation = Paths.get(lessonDirPath).resolve(fileName);
        try {
            // upload file if it does not exist in the directory
            if (!Files.exists(targetLocation)) {
                Files.copy(file.getInputStream(), targetLocation);
            }
            lesson.addFile(fileName);
        }
        catch (IOException ex) {
            return ResponseEntity.status(500).body("Error saving file: " + ex.getMessage());
        }
        lessonRepository.save(lesson);
        return ResponseEntity.ok("file '" + fileName + "' added to lesson with id " + lessonId + " successfully");
    }
    public ResponseEntity<String> updateLessonByRemoveFile (Integer lessonId, String fileName) {
        Optional<Lesson> optionalLesson = lessonRepository.findById(lessonId);
        if (optionalLesson.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("failed to update lesson\ninvalid lesson id");
        }
        Lesson lesson = optionalLesson.get();
        List<MediaFile> lessonFiles = lesson.getMediaFiles();
        // check if input file (fileName) exist in lesson with input id (lessonId)
        boolean exist = false;
        for (int i = 0; i < lessonFiles.size(); ++i) {
            if (lessonFiles.get(i).equals(fileName)) {
                exist = true;
                lesson.removeFile(i);
                break;
            }
        }
        if (!exist) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("failed to update lesson\nfile'" + fileName + "' not exist in lesson with id " + lessonId);
        }
        Integer courseId = lesson.getCourse().getId();
        String courseDirPath = System.getProperty("user.dir") + "/uploadedFiles" + "/course" + courseId + "files";
        String lessonDirPath = courseDirPath + "/lesson" + lesson.getId();
        Path filePath = Paths.get(lessonDirPath).resolve(fileName);
        try {
            Files.delete(filePath);
        }
        catch (IOException e) {
            System.out.println("An error occurred while deleting the file: " + e.getMessage());
        }
        lessonRepository.save(lesson);
        return ResponseEntity.ok("file '" + fileName + "' removed successfully from lesson with id " + lessonId);
    }
}
