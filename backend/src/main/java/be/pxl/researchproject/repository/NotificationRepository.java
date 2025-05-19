package be.pxl.researchproject.repository;

import be.pxl.researchproject.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findById(Long id);

    Optional<Notification> findByAnimalId(Long animalId);
}