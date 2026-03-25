package com.soen345.ticketreservation.unit.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.soen345.ticketreservation.exception.CancellationNotificationException;
import com.soen345.ticketreservation.exception.EmailConfirmationException;
import com.soen345.ticketreservation.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private JavaMailSender mailSender;

    private NotificationService notificationService;

    @Test
    void sendEmailConfirmation_Success() {
        notificationService = new NotificationService(mailSender);
        ReflectionTestUtils.setField(notificationService, "fromEmail", "noreply@example.com");
        notificationService.sendEmailConfirmation("test@example.com", "CONF123", "Test Event");

        SimpleMailMessage expectedMessage = new SimpleMailMessage();
        expectedMessage.setFrom("noreply@example.com");
        expectedMessage.setTo("test@example.com");
        expectedMessage.setSubject("Reservation Confirmed: Test Event");
        expectedMessage.setText("Your reservation for Test Event is confirmed.\nConfirmation code: CONF123");

        verify(mailSender).send(expectedMessage);
    }

    @Test
    void sendEmailConfirmation_Failure() {
        notificationService = new NotificationService(mailSender);
        ReflectionTestUtils.setField(notificationService, "fromEmail", "noreply@example.com");
        doThrow(new RuntimeException("SMTP error")).when(mailSender).send(any(SimpleMailMessage.class));

        assertThrows(EmailConfirmationException.class,
                () -> notificationService.sendEmailConfirmation("test@example.com", "CONF123", "Test Event"));

    }

    @Test
    void sendCancellationNotification_Success() {
        notificationService = new NotificationService(mailSender);
        ReflectionTestUtils.setField(notificationService, "fromEmail", "noreply@example.com");
        notificationService.sendCancellationNotification("test@example.com", "CONF123", "Test Event");

        SimpleMailMessage expectedMessage = new SimpleMailMessage();
        expectedMessage.setFrom("noreply@example.com");
        expectedMessage.setTo("test@example.com");
        expectedMessage.setSubject("Reservation Cancelled: Test Event");
        expectedMessage.setText("Your reservation for Test Event (code: CONF123) has been cancelled.");

        verify(mailSender).send(expectedMessage);
    }

    @Test
    void sendCancellationNotification_Failure() {
        notificationService = new NotificationService(mailSender);
        ReflectionTestUtils.setField(notificationService, "fromEmail", "noreply@example.com");
        doThrow(new RuntimeException("SMTP error")).when(mailSender).send(any(SimpleMailMessage.class));

        assertThrows(CancellationNotificationException.class,
                () -> notificationService.sendCancellationNotification("test@example.com", "CONF123", "Test Event"));
    }

}
