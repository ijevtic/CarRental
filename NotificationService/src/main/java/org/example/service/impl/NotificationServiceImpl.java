package org.example.service.impl;

import org.example.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    public NotificationServiceImpl() {
    }

    public void receiveNotification() {

    }
}
