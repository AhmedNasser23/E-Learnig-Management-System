package com.project.LMS.repository;

import com.project.LMS.entity.StudentNotification;
import com.project.LMS.entity.users.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    @Query("SELECT n FROM StudentNotification n WHERE n.Id = :notificationId")
    Optional<StudentNotification> findNotificationById(@Param("notificationId") int notificationId);
}
