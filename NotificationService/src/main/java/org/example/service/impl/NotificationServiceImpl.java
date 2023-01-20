package org.example.service.impl;

import org.example.domain.Notification;
import org.example.domain.Type;
import org.example.dto.*;
import org.example.dto.pop.PopActivateAccount;
import org.example.dto.pop.PopCancel;
import org.example.dto.pop.PopPassword;
import org.example.dto.pop.PopReservation;
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
        String typeName = null;
        String mailSubject = null;
        List<String> args = null;
        System.out.println(message);
        if(!message.getFirst().equals("ACTIVATE") && !message.getFirst().equals("CANCEL") &&
                !message.getFirst().equals("PASSWORD") && !message.getFirst().equals("RESERVATION")) {
            return;
        }

        Notification dbNotification = new Notification();
        MailDto mailDto = new MailDto();
        typeName = message.getFirst();
        Type type = typeRepository.findTypeByName(typeName).orElse(null);
        assert type != null;
        mailSubject = type.getSubject();
        mailDto.setClientSubject(mailSubject);
        mailDto.setManagerSubject(mailSubject);

        if(message.getFirst().equals("ACTIVATE")) {
            PopActivateAccount notification = (PopActivateAccount) message.getSecond();
            mailDto.setClientEmail(notification.getEmail());
            args = List.of(notification.getUsername(), notification.getToken());
            dbNotification.setClientEmail(notification.getEmail());
        }
        if(message.getFirst().equals("PASSWORD")) {
            PopPassword notification = (PopPassword) message.getSecond();
            mailDto.setClientEmail(notification.getEmail());
            args = List.of(notification.getUsername());
            dbNotification.setClientEmail(notification.getEmail());
        }
        if(message.getFirst().equals("RESERVATION")) {
            PopReservation notification = (PopReservation) message.getSecond();
            mailDto.setClientEmail(notification.getClientEmail());
            mailDto.setManagerEmail(notification.getManagerEmail());
            args = List.of(notification.getClientUsername(), notification.getModelName(), notification.getCity(),
                    notification.getCompanyName(), String.valueOf(notification.getStartTime()),
                    String.valueOf(notification.getEndTime()));
            dbNotification.setClientEmail(notification.getClientEmail());
            dbNotification.setManagerEmail(notification.getManagerEmail());
        }
        if(message.getFirst().equals("CANCEL")) {
            PopCancel notification = (PopCancel) message.getSecond();
            mailDto.setClientEmail(notification.getClientEmail());
            mailDto.setManagerEmail(notification.getManagerEmail());
            args = List.of(notification.getModelName(), notification.getCity(),
                    notification.getCompanyName(), String.valueOf(notification.getStartTime()),
                    String.valueOf(notification.getEndTime()));
            dbNotification.setClientEmail(notification.getClientEmail());
            dbNotification.setManagerEmail(notification.getManagerEmail());
        }
        mailDto.setClientMessage(makeText(type.getDescription(), args));
        mailDto.setManagerMessage(mailDto.getClientMessage());
        jmsTemplate.convertAndSend(mailQueue, messageHelper.createTextMessage(mailDto));
        dbNotification.setType(typeRepository.findTypeByName(typeName).orElse(null));
        dbNotification.setTime((int) (System.currentTimeMillis() / 1000));
        notificationRepository.save(dbNotification);
    }

    private String makeText(String pattern, List<String> list) {
        for(String s : list) {
            pattern = pattern.replaceFirst("%", s);
        }
        return pattern;
    }
}
