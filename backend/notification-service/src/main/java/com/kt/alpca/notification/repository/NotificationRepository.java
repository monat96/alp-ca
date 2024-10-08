package com.kt.alpca.notification.repository;

import com.kt.alpca.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "notifications", path = "notifications")
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
