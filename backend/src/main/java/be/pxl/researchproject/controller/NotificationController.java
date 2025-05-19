package be.pxl.researchproject.controller;

import be.pxl.researchproject.api.response.NotificationDTO;
import be.pxl.researchproject.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public NotificationDTO getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    @GetMapping(path = "/unreadnotifications")
    public Long getAmountOfUnreadNotifications() {
        return notificationService.getAmountOfUnreadMessages();
    }

    @PostMapping(path = "/updateallnotifications")
    public void changeAllNotificationsToRead() {
        notificationService.turnAllNotificationsToSeen();
    }

    @DeleteMapping(path = "/{animalId}/delete")
    public void deleteNotificationById(@PathVariable Long animalId) {
        notificationService.deleteNotifications(animalId);
    }
}