package com.project.LMS.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "Materials")
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String title;
    private String url;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
}
