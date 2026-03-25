package com.soen345.ticketreservation.service;

import com.soen345.ticketreservation.exception.CancellationNotificationException;
import com.soen345.ticketreservation.exception.EmailConfirmationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendEmailConfirmation(String to, String confirmationCode, String eventTitle) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject("Reservation Confirmed: " + eventTitle);
            message.setText("Your reservation for " + eventTitle
                    + " is confirmed.\nConfirmation code: " + confirmationCode);
            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailConfirmationException("Failed to send confirmation email to " + to, e);
        }
    }

    public void sendCancellationNotification(String to, String confirmationCode, String eventTitle) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject("Reservation Cancelled: " + eventTitle);
            message.setText("Your reservation for " + eventTitle
                    + " (code: " + confirmationCode + ") has been cancelled.");
            mailSender.send(message);
        } catch (Exception e) {
            throw new CancellationNotificationException("Failed to send cancellation email to " + to, e);
        }
    }
}
