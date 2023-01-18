package org.example.service;

public interface MailService {
    void sendEmail(String email);

    void sendSimpleMessage(String to, String subject, String text);
}
