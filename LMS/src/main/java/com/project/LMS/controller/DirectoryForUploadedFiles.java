package com.project.LMS.controller;

import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
public class DirectoryForUploadedFiles {
    private String directoryPath;
    // Constructor to create the uploadedFiles directory if it doesn't exist
    public DirectoryForUploadedFiles() {
        // Set directory path relative to the application's working directory
        this.directoryPath = System.getProperty("user.dir") + "/uploadedFiles";
        // Create a File object
        File directory = new File(directoryPath);
        // Check if the directory exists
        if (!directory.exists()) {
            // Attempt to create the directory
            if (directory.mkdirs()) {
                System.out.println("Directory created successfully: " + directoryPath);
            } else {
                System.out.println("Failed to create directory: " + directoryPath);
            }
        } else {
            System.out.println("Directory already exists: " + directoryPath);
        }
    }
}