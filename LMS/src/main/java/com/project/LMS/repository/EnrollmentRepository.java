package com.project.LMS.repository;

import com.project.LMS.entity.Enrollment;
import com.project.LMS.entity.StudentCourseKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, StudentCourseKey> {
}