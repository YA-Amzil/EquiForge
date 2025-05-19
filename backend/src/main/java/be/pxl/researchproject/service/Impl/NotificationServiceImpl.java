package be.pxl.researchproject.service.Impl;

import be.pxl.researchproject.api.response.NotificationDTO;
import be.pxl.researchproject.domain.*;
import be.pxl.researchproject.exception.NotificationNotFoundException;
import be.pxl.researchproject.repository.*;
import be.pxl.researchproject.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final HorseRepository horseRepository;
    private final FoalRepository foalRepository;

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(HorseRepository horseRepository, FoalRepository foalRepository,
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
        Notification notificationToRemove = notificationToDelete.get();
        notificationRepository.delete(notificationToRemove);
    }

    @Override
    @Transactional
    public void turnAllNotificationsToSeen() {
        List<Notification> allNotifications = notificationRepository.findAll().stream().toList();

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
        List<Notification> listOfNotifications = new ArrayList<>(notificationRepository.findAll().stream().toList());

        for (Horse horseToCheck : horses) {
            if (horseToCheck.getDaysThatHorseIsPregnant() >= 320 && !horseToCheck.isHasHadNotification()) {
                Notification newNotification = Notification.createHorseNotification(horseToCheck);
                if (!listOfNotifications.contains(newNotification)) {
                    listOfNotifications.add(newNotification);
                    horseToCheck.setHasHadNotification(true);
                    horseRepository.save(horseToCheck);
                    notificationRepository.save(newNotification);
                }
            }
        }

        for (Foal foal : foals) {
            if (foal.dewormNeededToday()) {
                Notification newNotification = Notification.createFoalNotification(foal);
                if (!listOfNotifications.contains(newNotification)) {
                    listOfNotifications.add(newNotification);
                    notificationRepository.save(newNotification);
                }
            }
        }

        List<Notification> seenNotifications = listOfNotifications.stream().filter(Notification::isHasBeenSeen)
                .toList();
        List<Notification> unseenNotifications = listOfNotifications.stream().filter(not -> !not.isHasBeenSeen())
                .toList();

        Long amountOfSeenNotifications = (long) seenNotifications.size();
        Long amountOfNotSeenNotifications = (long) unseenNotifications.size();
        return new NotificationDTO(amountOfNotSeenNotifications, amountOfSeenNotifications, unseenNotifications,
                seenNotifications);

    }

    @Override
    @Transactional
    public Long getAmountOfUnreadMessages() {
        NotificationDTO notificationDTO = createAllNotifications();
        return notificationDTO.amountOfUnreadNotifications();
    }
}