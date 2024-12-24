package com.project.LMS.service;

import com.project.LMS.service.EmailSenderService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

public class EmailSenderServiceTest {

    @InjectMocks
    private EmailSenderService emailSenderService;

    @Mock
    private JavaMailSender mailSender;

    public EmailSenderServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("wwww.faragello@gmail.com");
        message.setTo("test@example.com");
        message.setSubject("Test Subject");
        message.setText("Test Body");

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailSenderService.sendEmail("test@example.com", "Test Subject", "Test Body");

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
