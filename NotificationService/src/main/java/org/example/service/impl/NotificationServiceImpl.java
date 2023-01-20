package org.example.service.impl;

import org.example.domain.Notification;
import org.example.domain.NotificationReminder;
import org.example.domain.Type;
import org.example.dto.*;
import org.example.dto.pop.PopActivateAccount;
import org.example.dto.pop.PopCancel;
import org.example.dto.pop.PopPassword;
import org.example.dto.pop.PopReservation;
import org.example.listener.helper.MessageHelper;
import org.example.repository.NotificationReminderRepository;
import org.example.repository.NotificationRepository;
import org.example.repository.TypeRepository;
import org.example.service.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private NotificationRepository notificationRepository;
    private NotificationReminderRepository notificationReminderRepository;
    private TypeRepository typeRepository;
    private JmsTemplate jmsTemplate;
    private MessageHelper messageHelper;
    private String mailQueue;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   NotificationReminderRepository notificationReminderRepository,
                                   TypeRepository typeRepository,
                                   JmsTemplate jmsTemplate, MessageHelper messageHelper,
                                   @Value("${async.sendEmails}") String mailQueue) {
        this.notificationRepository = notificationRepository;
        this.notificationReminderRepository = notificationReminderRepository;
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

            createNotificationReminder(notification.getReservationId(), notification.getClientEmail(),
                    notification.getManagerEmail(), notification.getClientUsername(), notification.getModelName(),
                    notification.getCity(), notification.getCompanyName(), notification.getStartTime());
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

            deleteNotificationReminder(notification.getReservationId());
        }
        mailDto.setClientMessage(makeText(type.getDescription(), args));
        mailDto.setManagerMessage(mailDto.getClientMessage());
        jmsTemplate.convertAndSend(mailQueue, messageHelper.createTextMessage(mailDto));
        dbNotification.setType(typeRepository.findTypeByName(typeName).orElse(null));
        dbNotification.setTime((int) (System.currentTimeMillis() / 1000));
        notificationRepository.save(dbNotification);
    }

    @Scheduled(fixedDelay = 3600000) // 1 hour
    public void scheduleFixedDelayTask() {
        List<NotificationReminder> reminders = notificationReminderRepository.findAll();
        Integer nowTime = (int) (System.currentTimeMillis() / 1000);
        for(NotificationReminder reminder: reminders) {
            if(nowTime + 86400 > reminder.getStartTime()) {
                notificationReminderRepository.deleteById(reminder.getId());
                sendReminderEmail(reminder.getClientEmail(), reminder.getManagerEmail(), reminder.getClientUsername(),
                        reminder.getModelName(), reminder.getCity(), reminder.getCompanyName());
            }
        }
    }

    private String makeText(String pattern, List<String> list) {
        for(String s : list) {
            pattern = pattern.replaceFirst("%", s);
        }
        return pattern;
    }

    private void createNotificationReminder(Long reservationId, String clientEmail, String managerEmail,
            String clientUsername, String modelName, String city, String companyName, Integer startTime) {
        Integer nowTime = (int) (System.currentTimeMillis() / 1000);
        if(nowTime + 86400 > startTime) //Notification day before
            return;
        NotificationReminder notificationReminder = new NotificationReminder(reservationId, clientEmail,
                managerEmail, clientUsername, modelName, city, companyName, startTime);
        notificationReminderRepository.save(notificationReminder);

    }

    private void deleteNotificationReminder(Long reservationId) {
        notificationReminderRepository.deleteById(reservationId);
    }

    private void sendReminderEmail(String clientEmail, String managerEmail, String clientUsername, String modelName,
                                   String city, String companyName) {
        Type type = typeRepository.findTypeByName("REMINDER").orElse(null);
        assert type != null;
        MailDto reminderMail = new MailDto();
        reminderMail.setClientEmail(clientEmail);
        reminderMail.setClientSubject(type.getSubject());
        String message = makeText(type.getDescription(), List.of(clientUsername, modelName,city,companyName));
        reminderMail.setClientMessage(message);
        reminderMail.setManagerEmail(managerEmail);
        reminderMail.setManagerSubject(type.getSubject());
        reminderMail.setManagerMessage(message);
        jmsTemplate.convertAndSend(mailQueue, messageHelper.createTextMessage(reminderMail));
    }
}
