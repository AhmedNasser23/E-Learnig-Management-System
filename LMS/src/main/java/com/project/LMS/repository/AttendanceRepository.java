package com.project.LMS.repository;

import com.project.LMS.entity.Attendance;
import com.project.LMS.entity.StudentLessonKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, StudentLessonKey> {
}