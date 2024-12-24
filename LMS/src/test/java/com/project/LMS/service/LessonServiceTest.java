package com.project.LMS.service;
import com.project.LMS.entity.Course;
import com.project.LMS.entity.Lesson;
import com.project.LMS.entity.users.MediaFile;
import com.project.LMS.repository.CourseRepository;
import com.project.LMS.repository.LessonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@ExtendWith(MockitoExtension.class)
public class LessonServiceTest {
    @Mock
    private LessonRepository lessonRepository;
    @Mock
    private CourseRepository courseRepository;
    @InjectMocks
    private LessonService lessonService;
    @Test
    public void testCreateLessonWithInvalidCourseId() {
        Integer courseId = 999;
        Mockito.when(courseRepository.findById(courseId)).thenReturn(Optional.empty());
        ResponseEntity<String> response = lessonService.createLesson(new ArrayList<>(), "Lesson Title", courseId);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains("invalid course id"));
    }
    @Test
    public void testCreateLessonSuccess() {
        Integer courseId = 1;
        String title = "Lesson Title";
        List<MultipartFile> files = List.of(new MockMultipartFile("file1.txt", "Hello".getBytes()));
        Course mockCourse = new Course();
        mockCourse.setId(courseId);
        Lesson mockLesson = new Lesson();
        mockLesson.setId(1);
        Mockito.when(courseRepository.findById(courseId)).thenReturn(Optional.of(mockCourse));
        Mockito.when(lessonRepository.save(Mockito.any(Lesson.class))).thenReturn(mockLesson);
        ResponseEntity<String> response = lessonService.createLesson(files, title, courseId);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains("lesson created successfully"));
        Mockito.verify(lessonRepository, Mockito.times(1)).save(Mockito.any(Lesson.class));
    }
    @Test
    public void testDeleteLessonWithInvalidLessonId() {
        Integer lessonId = 999;
        Mockito.when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());
        ResponseEntity<String> response = lessonService.deleteLesson(lessonId);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains("invalid lesson id"));
    }
    @Test
    public void testDeleteLessonSuccess() {
        Integer lessonId = 1;
        Lesson mockLesson = new Lesson();
        Course mockCourse = new Course();
        mockCourse.setId(1);
        mockLesson.setCourse(mockCourse);
        mockLesson.setMediaFiles(List.of(new MediaFile("file1.txt")));
        Mockito.when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(mockLesson));
        ResponseEntity<String> response = lessonService.deleteLesson(lessonId);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains("has been deleted"));
        Mockito.verify(lessonRepository, Mockito.times(1)).deleteById(lessonId);
    }
    @Test
    public void testUpdateLessonTitleWithInvalidLessonId() {
        Integer lessonId = 999;
        String newTitle = "New Title";
        Mockito.when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());
        ResponseEntity<String> response = lessonService.updateLessonTitle(lessonId, newTitle);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains("invalid lesson id"));
    }
    @Test
    public void testUpdateLessonTitleSuccess() {
        Integer lessonId = 1;
        String newTitle = "New Title";
        Lesson mockLesson = new Lesson();
        mockLesson.setId(lessonId);
        Mockito.when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(mockLesson));
        Mockito.when(lessonRepository.save(mockLesson)).thenReturn(mockLesson);
        ResponseEntity<String> response = lessonService.updateLessonTitle(lessonId, newTitle);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains("updated successfully"));
        Mockito.verify(lessonRepository, Mockito.times(1)).save(mockLesson);
    }
}