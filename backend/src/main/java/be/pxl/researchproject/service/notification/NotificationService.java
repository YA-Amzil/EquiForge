package be.pxl.researchproject.service.notification;

import be.pxl.researchproject.api.response.NotificationDTO;

public interface NotificationService {
    void updateNotifications(Long id);
    void deleteNotifications(Long id);
    void turnAllNotificationsToSeen();
    NotificationDTO getAllNotifications();
    NotificationDTO createAllNotifications();
    Long getAmountOfUnreadMessages();
}