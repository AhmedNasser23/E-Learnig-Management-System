package com.project.LMS.repository;

import com.project.LMS.entity.StudentNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentNotificationRepository extends JpaRepository<StudentNotification, Integer> {}