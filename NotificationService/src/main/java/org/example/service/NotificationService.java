package org.example.service;

import org.example.dto.ANotification;
import org.springframework.data.util.Pair;

public interface NotificationService {
    void receiveNotification(Pair<String, ANotification> notification);
}
