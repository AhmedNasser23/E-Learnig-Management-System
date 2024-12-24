package com.project.LMS.service;

import com.project.LMS.dao.Course.CourseDao;
import com.project.LMS.dao.Users.AdminDao;
import com.project.LMS.dao.Users.InstructorDao;
import com.project.LMS.dao.Users.StudentDao;
import com.project.LMS.entity.Course;
import com.project.LMS.entity.users.Admin;
import com.project.LMS.entity.users.Instructor;
import com.project.LMS.entity.users.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private AdminDao adminRepository;

    @Mock
    private StudentDao studentRepository;

    @Mock
    private InstructorDao instructorRepository;

    @Mock
    private CourseDao courseRepository;

    @InjectMocks
    private AdminService adminService;

    @Test
    void testAddStudent() {
        Student student = new Student();
        student.setId(1);
        student.setName("John Doe");

        adminService.addStudent(student);
        verify(studentRepository, times(1)).addStudent(student);
    }

    @Test
    void testAddInstructor() {
        Instructor instructor = new Instructor();
        instructor.setId(1);
        instructor.setName("Jane Doe");

        adminService.addInstructor(instructor);
        verify(instructorRepository, times(1)).addInstructor(instructor);
    }

    @Test
    void testAddAdmin() {
        Admin admin = new Admin();
        admin.setId(1);
        admin.setName("Admin User");

        adminService.addAdmin(admin);
        verify(adminRepository, times(1)).addAdmin(admin);
    }

    @Test
    void testAddCourse() {
        Course course = new Course();
        course.setId(101);
        course.setTitle("Java Basics");

        adminService.addCourse(course);
        verify(courseRepository, times(1)).addCourse(course);
    }

    @Test
    void testDeleteStudent() {
        adminService.deleteStudent(1);
        verify(studentRepository, times(1)).deleteStudent(1);
    }

    @Test
    void testDeleteInstructor() {
        adminService.deleteInstructor(1);
        verify(instructorRepository, times(1)).deleteInstructor(1);
    }

    @Test
    void testDeleteAdmin() {
        adminService.deleteAdmin(1);
        verify(adminRepository, times(1)).deleteAdmin(1);
    }

    @Test
    void testDeleteCourse() {
        adminService.deleteCourse(101);
        verify(courseRepository, times(1)).deleteCourse(101);
    }
}
