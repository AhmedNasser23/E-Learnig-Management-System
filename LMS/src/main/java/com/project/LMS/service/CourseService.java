package com.project.LMS.service;

import com.project.LMS.entity.Course;
import com.project.LMS.entity.Lesson;
import com.project.LMS.entity.StudentCourseKey;
import com.project.LMS.entity.users.MediaFile;
import com.project.LMS.repository.CourseRepository;
import com.project.LMS.repository.EnrollmentRepository;
import com.project.LMS.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.File;
import java.net.URLConnection;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private LessonRepository lessonRepository;

    public Course createCourse(Course course) {
        Course createdCourse = courseRepository.save(course);
        // create folder for course files
        String courseDirPath = System.getProperty("user.dir") + "/uploadedFiles" + "/course" + createdCourse.getId() + "files";
        File directory = new File(courseDirPath);
        if (directory.mkdirs()) {
            System.out.println("Directory created successfully: " + courseDirPath);
        } else {
            System.out.println("Failed to create directory: " + courseDirPath);
        }
        return createdCourse;
    }

    public ResponseEntity<String> deleteCourse(Integer id) {
        Optional<Course> course = courseRepository.findById(id);
        if (course.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("invalid course id");
        }
        courseRepository.deleteById(id);
        String courseDirPath = System.getProperty("user.dir") + "/uploadedFiles" + "/course" + id + "files";
        File directory = new File(courseDirPath);
        deleteDirectory(directory);
        return ResponseEntity.ok("Course with ID " + id + " has been deleted");
    }
    private boolean deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectory(file); // Recursively delete subdirectories and files
                }
            }
        }
        return directory.delete(); // Delete the directory or file
    }

    public Course updateCourseTitle (Integer id, String title) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isEmpty()) {
            return null;
        }
        Course course = optionalCourse.get();
        course.setTitle(title);
        courseRepository.save(course);
        return course;
    }

    public Course updateCourseDescription (Integer id, String description) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isEmpty()) {
            return null;
        }
        Course course = optionalCourse.get();
        course.setDescription(description);
        courseRepository.save(course);
        return course;
    }

    public Course updateCourseDuration (Integer id, Double duration) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isEmpty()) {
            return null;
        }
        Course course = optionalCourse.get();
        course.setDuration(duration);
        courseRepository.save(course);
        return course;
    }

    public String getAllCourses () {
        List<Course> courses = courseRepository.findAll();
        String ans = "";
        for (Course c : courses) {
            ans += "Course ID: " + c.getId() + "\n";
            ans += "Course Title: " + c.getTitle() + "\n";
            ans += "Course Description: " + c.getDescription() + "\n";
            ans += "Course Duration: " + c.getDuration() + "\n";
            ans += "\n";
        }
        return ans;
    }

    public ResponseEntity<String> getCourseLessons (Integer courseId) {
        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("invalid course id");
        }
        String ans = "";
        List<Lesson> lessons = course.get().getLessons();
        for (Lesson l : lessons) {
            ans += "Lesson ID: " + l.getId() + "\n";
            ans += "Lesson title: " + l.getTitle() + "\n";
            ans += "Lesson files: ";
            List<MediaFile> files = l.getMediaFiles();
            for (MediaFile file : files) {
                ans += file.getFileName() + "  ";
            }
            ans += "\n\n";
        }
        return ResponseEntity.ok(ans);
    }
    public ResponseEntity displayFile(String fileName, Integer courseId, Integer lessonId, Integer studentId) {
        Optional<Course> course = courseRepository.findById(courseId);
        // check if course exist or not
        if (course.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("invalid course id");
        }
        // check if lessonId exist in course
        if (lessonRepository.findById(lessonId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("course with Id " + courseId + " does not have lesson with id " + lessonId);
        }
        // check if studentId enrolled in course
        if (enrollmentRepository.findById(new StudentCourseKey(studentId, courseId)).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("student with Id " + studentId + " not enrolled in course with id " + courseId);
        }
        // display file
        String filePath = System.getProperty("user.dir") + "/uploadedFiles" + "/course" + courseId + "files" +  "/lesson" + lessonId + "/" + fileName;
        File file = new File(filePath);
        if (file.exists()) {
            Resource resource = new FileSystemResource(file);
            String contentType = URLConnection.guessContentTypeFromName(fileName);
            if (contentType == null) {
                contentType = "application/octet-stream"; // Default to binary stream
            }
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"");
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("file '" + fileName + "' not exist");

        }
    }


}