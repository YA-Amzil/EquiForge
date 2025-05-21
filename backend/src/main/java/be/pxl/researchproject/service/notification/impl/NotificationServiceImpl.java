package be.pxl.researchproject.service.notification.impl;

import be.pxl.researchproject.api.response.NotificationDTO;
import be.pxl.researchproject.domain.*;
import be.pxl.researchproject.exception.NotificationNotFoundException;
import be.pxl.researchproject.repository.*;
import be.pxl.researchproject.service.notification.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final HorseRepository horseRepository;
    private final FoalRepository foalRepository;
    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(
            HorseRepository horseRepository, 
            FoalRepository foalRepository,
            NotificationRepository notificationRepository) {
        this.horseRepository = horseRepository;
        this.foalRepository = foalRepository;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void updateNotifications(Long id) {
        Optional<Notification> notificationToChange = notificationRepository.findById(id);
        if (notificationToChange.isEmpty()) {
            throw new NotificationNotFoundException("Notification with id " + id + " does not exist");
        }
        Notification notificationToUpdate = notificationToChange.get();
        notificationToUpdate.setHasBeenSeen(true);
    }

    @Override
    public void deleteNotifications(Long id) {
        Optional<Notification> notificationToDelete = notificationRepository.findByAnimalId(id);
        if (notificationToDelete.isEmpty()) {
            throw new NotificationNotFoundException("Notification with id " + id + " does not exist");
        }
        notificationRepository.delete(notificationToDelete.get());
    }

    @Override
    @Transactional
    public void turnAllNotificationsToSeen() {
        List<Notification> allNotifications = notificationRepository.findAll();
        for (Notification notification : allNotifications) {
            notification.setHasBeenSeen(true);
            notificationRepository.save(notification);
        }
    }

    @Override
    @Transactional
    public NotificationDTO getAllNotifications() {
        return createAllNotifications();
    }

    @Override
    @Transactional
    public NotificationDTO createAllNotifications() {
        List<Horse> horses = horseRepository.findAll();
        List<Foal> foals = foalRepository.findAll();
        List<Notification> notifications = new ArrayList<>(notificationRepository.findAll());

        processHorseNotifications(horses, notifications);
        processFoalNotifications(foals, notifications);

        List<Notification> seenNotifications = notifications.stream()
                .filter(Notification::isHasBeenSeen)
                .toList();
        List<Notification> unseenNotifications = notifications.stream()
                .filter(not -> !not.isHasBeenSeen())
                .toList();

        return new NotificationDTO(
                (long) unseenNotifications.size(),
                (long) seenNotifications.size(),
                unseenNotifications,
                seenNotifications);
    }

    private void processHorseNotifications(List<Horse> horses, List<Notification> notifications) {
        for (Horse horse : horses) {
            if (horse.getDaysThatHorseIsPregnant() >= 320 && !horse.isHasHadNotification()) {
                Notification newNotification = Notification.createHorseNotification(horse);
                if (!notifications.contains(newNotification)) {
                    notifications.add(newNotification);
                    horse.setHasHadNotification(true);
                    horseRepository.save(horse);
                    notificationRepository.save(newNotification);
                }
            }
        }
    }

    private void processFoalNotifications(List<Foal> foals, List<Notification> notifications) {
        for (Foal foal : foals) {
            if (foal.dewormNeededToday()) {
                Notification newNotification = Notification.createFoalNotification(foal);
                if (!notifications.contains(newNotification)) {
                    notifications.add(newNotification);
                    notificationRepository.save(newNotification);
                }
            }
        }
    }

    @Override
    @Transactional
    public Long getAmountOfUnreadMessages() {
        NotificationDTO notificationDTO = createAllNotifications();
        return notificationDTO.amountOfUnreadNotifications();
    }
}