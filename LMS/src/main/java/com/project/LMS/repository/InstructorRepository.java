package com.project.LMS.repository;

import com.project.LMS.entity.InstructorNotification;
import com.project.LMS.entity.users.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InstructorRepository extends JpaRepository<Instructor, Integer> {

    @Query("SELECT n FROM InstructorNotification n WHERE n.Id = :notificationId")
    Optional<InstructorNotification> findNotificationById(@Param("notificationId") int notificationId);
}

