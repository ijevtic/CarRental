package org.example.listener;

import org.example.listener.helper.MessageHelper;
import org.example.service.NotificationService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;

@Component
public class EmailListener {

    private MessageHelper messageHelper;
    private NotificationService notificationService;

    public EmailListener(MessageHelper messageHelper, NotificationService notificationService) {
        this.messageHelper = messageHelper;
        this.notificationService = notificationService;
    }

    @JmsListener(destination = "${async.notifications}", concurrency = "5-10")
    public void addOrder(Message message) throws JMSException {
//        MatchesDto matchesDto = messageHelper.getMessage(message, MatchesDto.class);
        notificationService.receiveNotification();
    }
}

