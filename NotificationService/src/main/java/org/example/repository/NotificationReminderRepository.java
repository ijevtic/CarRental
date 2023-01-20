package org.example.repository;

import org.example.domain.Notification;
import org.example.domain.NotificationReminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationReminderRepository extends JpaRepository<NotificationReminder, Long> {

}
