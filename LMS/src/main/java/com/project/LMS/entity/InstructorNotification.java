package com.project.LMS.entity;

import com.project.LMS.entity.users.Instructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "InstructorNotifications")
public class InstructorNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Id;

    @Column(name = "message", nullable = false, length = 1000)
    private String message;

    @ManyToOne
    @JoinColumn(name = "instructor_Id", nullable = false)
    private Instructor instructor;

    @Column(name = "isRead", nullable = false)
    private boolean isRead = false;

    public InstructorNotification(String message, Instructor instructor) {
        this.message = message;
        this.instructor = instructor;
    }

}