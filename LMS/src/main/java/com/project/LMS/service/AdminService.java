package com.project.LMS.service;

import com.project.LMS.dao.Users.AdminDao;
import com.project.LMS.dao.Course.CourseDao;
import com.project.LMS.dao.Users.InstructorDao;
import com.project.LMS.dao.Users.StudentDao;
import com.project.LMS.entity.*;

import com.project.LMS.entity.users.Admin;
import com.project.LMS.entity.users.Instructor;
import com.project.LMS.entity.users.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AdminService {

    @Autowired
    private final AdminDao adminRepository;
    private final StudentDao studentRepository;
    private final InstructorDao instructorRepository;
    private final CourseDao courseRepository;

    public AdminService(AdminDao adminRepository, StudentDao studentRepository, InstructorDao instructorRepository, CourseDao courseRepository) {
        this.adminRepository = adminRepository;
        this.studentRepository = studentRepository;
        this.instructorRepository = instructorRepository;
        this.courseRepository = courseRepository;
    }

    public void addStudent(Student student) {
        studentRepository.addStudent(student);
    }
    public void addInstructor(Instructor instructor) {
        instructorRepository.addInstructor(instructor);
    }
    public void addAdmin(Admin admin) {
        adminRepository.addAdmin(admin);
    }
    public void addCourse(Course course) {
        courseRepository.addCourse(course);
    }

    public void deleteStudent(int id) {
        studentRepository.deleteStudent(id);
    }
    public void deleteInstructor(int id) {
        instructorRepository.deleteInstructor(id);
    }
    public void deleteAdmin(int id) {
        adminRepository.deleteAdmin(id);
    }
    public void deleteCourse(int id) {
        courseRepository.deleteCourse(id);
    }
}

