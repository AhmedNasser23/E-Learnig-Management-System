package com.project.LMS.entity.users;

import com.project.LMS.entity.Lesson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MediaFiles")
public class MediaFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int fileId;
    @Column(name = "fileName")
    String fileName;

    @ManyToOne
    @JoinColumn(name= "lesson_id", nullable = false)
    private Lesson lesson;

    public MediaFile(String fileName) {
        this.fileName = fileName;
    }
}
