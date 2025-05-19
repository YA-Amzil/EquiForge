package be.pxl.researchproject.api.response;

import be.pxl.researchproject.domain.Notification;

import java.util.List;

public record NotificationDTO(Long amountOfUnreadNotifications,
                Long amountOfReadNotifications,

                List<Notification> unreadNotificationList,
                List<Notification> readNotificationsList) {
}