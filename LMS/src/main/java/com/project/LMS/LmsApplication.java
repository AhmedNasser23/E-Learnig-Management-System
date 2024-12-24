package com.project.LMS;

import com.project.LMS.entity.users.Student;
import com.project.LMS.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class LmsApplication {
//	@Autowired
//	private EmailSenderService senderService;
	public static void main(String[] args) {
		SpringApplication.run(LmsApplication.class, args);
	}


//	@EventListener(ApplicationReadyEvent.class)
//	public void sendEmail() {
////		Student student = new Student();
//		senderService.sendEmail(student.getEmail(), "LMS Notification", message);
////        senderService.sendEmail("ahmadhemdan280@gmail.com", "LMS Notification", "Test Email Notification");
//	}
}
