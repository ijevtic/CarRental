package org.example.listener.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.ANotification;
import org.example.dto.PopActivateAccount;
import org.example.dto.NotificationMQ;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MessageHelper {
    private Validator validator;
    private ObjectMapper objectMapper;

    public MessageHelper(Validator validator, ObjectMapper objectMapper) {
        this.validator = validator;
        this.objectMapper = objectMapper;
    }

    public Pair<String, ANotification> getMessage(Message message) throws RuntimeException, JMSException {
        try {
            String json = ((TextMessage) message).getText();
            System.out.println(json);
            NotificationMQ data = objectMapper.readValue(json, NotificationMQ.class);
            if(data.getType().equals("ACTIVATE")) {
                System.out.println("usooooo");
                NotificationMQ<PopActivateAccount> notificationMQ = objectMapper.readValue(json,
                        new TypeReference<NotificationMQ<PopActivateAccount>>() {});
                return Pair.of(data.getType(), notificationMQ.getData());
            }


        } catch (IOException exception) {
            exception.printStackTrace();
            throw new RuntimeException("Message parsing fails.", exception);
        }
        return null;
    }


    public String createTextMessage(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Problem with creating text message");
        }
    }

    private <T> void printViolationsAndThrowException(Set<ConstraintViolation<T>> violations) {
        String concatenatedViolations = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        throw new RuntimeException(concatenatedViolations);
    }
}
