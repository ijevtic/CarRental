package org.example.listener;

import org.example.dto.MailDto;
import org.example.listener.helper.MessageHelper;
import org.example.service.MailService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;

@Component
public class EmailListener {

    private MessageHelper messageHelper;
    private MailService emailService;

    public EmailListener(MessageHelper messageHelper, MailService emailService) {
        this.messageHelper = messageHelper;
        this.emailService = emailService;
    }

    @JmsListener(destination = "${async.sendEmails}", concurrency = "5-10")
    public void sendNotification(Message message) throws JMSException {
        MailDto mailDto = messageHelper.getMessage(message, MailDto.class);
        emailService.sendSimpleMessage(mailDto.getClientEmail(), mailDto.getClientSubject(), mailDto.getClientMessage());
        if(mailDto.getManagerEmail() != null)
            emailService.sendSimpleMessage(mailDto.getManagerEmail(), mailDto.getManagerSubject(), mailDto.getManagerMessage());
    }
}
