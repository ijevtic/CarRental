package org.example.service.impl;

import org.example.domain.Type;
import org.example.dto.*;
import org.example.listener.helper.MessageHelper;
import org.example.repository.NotificationRepository;
import org.example.repository.TypeRepository;
import org.example.service.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private NotificationRepository notificationRepository;
    private TypeRepository typeRepository;
    private JmsTemplate jmsTemplate;
    private MessageHelper messageHelper;
    private String mailQueue;

    public NotificationServiceImpl(NotificationRepository notificationRepository, TypeRepository typeRepository,
                                   JmsTemplate jmsTemplate, MessageHelper messageHelper,
                                   @Value("${async.sendEmails}") String mailQueue) {
        this.notificationRepository = notificationRepository;
        this.typeRepository = typeRepository;
        this.jmsTemplate = jmsTemplate;
        this.messageHelper = messageHelper;
        this.mailQueue = mailQueue;

    }

    public void receiveNotification(Pair<String, ANotification> message) {
        MailDto mailDto = null;
        String typeName = null;
        mailDto = new MailDto();
        String mailSubject = null;
        List<String> args = null;

        if(message.getFirst().equals("ACTIVATE")) {
            typeName = "ACTIVATE";
            PopActivateAccount notification = (PopActivateAccount) message.getSecond();
            mailDto.setClientEmail(notification.getEmail());
            args = List.of(notification.getUsername(), notification.getToken());
        }
        if(message.getFirst().equals("PASSWORD")) {
            typeName = "PASSWORD";
            PopPassword notification = (PopPassword) message.getSecond();
            mailDto.setClientEmail(notification.getEmail());
            args = List.of(notification.getUsername());
        }
        if(message.getFirst().equals("RESERVATION")) {
            typeName = "RESERVATION";
            PopReservation notification = (PopReservation) message.getSecond();
            mailDto.setClientEmail(notification.getClientEmail());
            mailDto.setManagerEmail(notification.getManagerEmail());
            args = List.of(notification.getClientUsername(), notification.getModelName(), notification.getCity(),
                    notification.getCompanyName(), String.valueOf(notification.getStartTime()),
                    String.valueOf(notification.getEndTime()));
        }
        if(mailDto == null)
            return;
        Type type = typeRepository.findTypeByName(typeName).orElse(null);
        assert type != null;
        mailSubject = type.getSubject();
        mailDto.setClientSubject(mailSubject);
        mailDto.setClientMessage(makeText(type.getDescription(), args));
        jmsTemplate.convertAndSend(mailQueue, messageHelper.createTextMessage(mailDto));
    }

    private String makeText(String pattern, List<String> list) {
        for(String s : list) {
            pattern = pattern.replaceFirst("%", s);
        }
        return pattern;
    }
}
