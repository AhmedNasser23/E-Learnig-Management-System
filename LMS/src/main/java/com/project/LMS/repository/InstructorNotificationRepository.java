package com.project.LMS.repository;

import com.project.LMS.entity.InstructorNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstructorNotificationRepository extends JpaRepository<InstructorNotification, Integer> {}