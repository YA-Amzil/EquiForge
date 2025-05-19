package be.pxl.researchproject.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long animalId;
    private String description;

    private boolean hasBeenSeen;

    private LocalDate notificationCreated;

    public long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(long animalId) {
        this.animalId = animalId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isHasBeenSeen() {
        return hasBeenSeen;
    }

    public void setHasBeenSeen(boolean hasBeenSeen) {
        this.hasBeenSeen = hasBeenSeen;
    }

    public LocalDate getNotificationCreated() {
        return notificationCreated;
    }

    public void setNotificationCreated(LocalDate notificationCreated) {
        this.notificationCreated = notificationCreated;
    }

    public static Notification createHorseNotification(Horse horse) {
        LocalDate dayOfPregnancy = horse.getDatePregnant().plusDays(320);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDayOfPregnancy = dayOfPregnancy.format(formatter);

        Notification horseNotification = new Notification();
        horseNotification.setNotificationCreated(LocalDate.now());
        horseNotification.setAnimalId(horse.getId());
        horseNotification.setDescription(horse.getName() + " - 320 dagen drachtig vanaf " + formattedDayOfPregnancy);
        return horseNotification;
    }

    public static Notification createFoalNotification(Foal foal) {
        Notification foalNotification = new Notification();
        foalNotification.setNotificationCreated(LocalDate.now());
        foalNotification.setAnimalId(foal.getId());
        foalNotification.setDescription(foal.getName() + " moet vandaag ontwormd worden");
        return foalNotification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Notification that = (Notification) o;
        return animalId == that.animalId && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(animalId, description);
    }
}