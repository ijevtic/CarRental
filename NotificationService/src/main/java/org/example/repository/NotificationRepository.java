package org.example.repository;

import org.example.domain.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findAllByClientEmail(String clientEmail, Pageable pageable);

    Page<Notification> findAllByManagerEmail(String managerEmail, Pageable pageable);

    Page<Notification> findAllByClientEmailAndTimeBetween(String clientEmail, Integer start, Integer end, Pageable pageable);

    Page<Notification> findAllByManagerEmailAndTimeBetween(String managerEmail, Integer start, Integer end, Pageable pageable);
}
