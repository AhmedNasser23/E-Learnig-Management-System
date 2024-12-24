package com.project.LMS.entity;
import com.project.LMS.entity.users.MediaFile;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String title;
//    private List<String> mediaFiles;
    private String otp;
    private LocalDateTime otpExpiration;

//    public MediaFile

//    public Lesson () {
//        mediaFiles = new ArrayList<>() ;
//    }
    // Many Lessons can belong to one Course
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)  // course_id -> foreign key
    private Course course;

    @OneToMany
    private List<MediaFile> mediaFiles = new ArrayList<>();

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendance> attendances ;

    public void addFile(String fileName) {
        mediaFiles.add(new MediaFile(fileName));
    }
    public void removeFile (int index) {
        mediaFiles.remove(index);
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
    public Course getCourse() {
        return course;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }

    public void setMediaFiles(List<MediaFile> mediaFiles) {
        this.mediaFiles = mediaFiles;
    }
    public List<MediaFile> getMediaFiles () {
        return mediaFiles;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
    public String getOtp() {
        return otp;
    }
    public void setOtpExpiration(LocalDateTime otpExpiration) {
        this.otpExpiration = otpExpiration;
    }
    public LocalDateTime getOtpExpiration() {
        return otpExpiration;
    }
}