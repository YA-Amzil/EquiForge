package be.pxl.researchproject.domain;

import be.pxl.researchproject.exception.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "horses")
public class Horse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDate birthDate;
    private double height;
    private boolean isPregnant;

    private LocalDate datePregnant;
    private LocalDate expectedPregnancyDate;
    private String studHorse;
    @ElementCollection(fetch = FetchType.EAGER)
    private Map<LocalDate, Boolean> plannedDates;

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "name")
    @Column(name = "text")
    @CollectionTable(name = "horse_birth_diaries", joinColumns = @JoinColumn(name = "horse_id"))
    private Map<String, String> birthDiaries;

    @OneToMany(fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Foal> foals;

    private boolean hasHadNotification;

    public Horse() {
        // JPA Only!
    }

    public Horse(String name, LocalDate birthDate, double height, boolean isPregnant, String studHorse,
            LocalDate datePregnant) {
        this.name = name;
        this.birthDate = birthDate;
        this.height = height;
        this.isPregnant = isPregnant;
        setPregnantAndPregnancyDate(datePregnant);
        this.studHorse = studHorse;
        this.birthDiaries = new HashMap<>();
        this.foals = new ArrayList<>();
        this.plannedDates = new HashMap<>();
    }

    private void setPregnantAndPregnancyDate(LocalDate datePregnant) {
        if (!isPregnant() || datePregnant == null) {
            this.datePregnant = null;
        } else {
            setDatePregnant(datePregnant);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthdate) {
        this.birthDate = birthdate;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public boolean isPregnant() {
        return isPregnant;
    }

    public void setPregnant(boolean pregnant) {
        this.isPregnant = pregnant;
    }

    public LocalDate getDatePregnant() {
        return datePregnant;
    }

    public void setDatePregnant(LocalDate datePregnant) {
        this.datePregnant = datePregnant;
        if (this.datePregnant != null) {
            setExpectedPregnancyDate(datePregnant.plusDays(320));
        }
    }

    public List<Foal> getFoals() {
        return foals;
    }


    public boolean isHasHadNotification() {
        return hasHadNotification;
    }

    public void setHasHadNotification(boolean hasHadNotification) {
        this.hasHadNotification = hasHadNotification;
    }

    public void addFoal(Foal foal) {
        if(this.foals == null){
            this.foals = new ArrayList<Foal>();
        }
        this.foals.add(foal);
    }

    public void removeFoalById(Long id) {
        boolean found = foals.removeIf(foal -> foal.getId().equals(id));
    }

    public void createDiary(String diaryName) {
        updateDiary(diaryName, "");
    }

    public void updateDiary(String diaryName, String text) {
        this.birthDiaries.put(diaryName, text);
    }

    public Map<String, String> getBirthDiaries() {
        return birthDiaries;
    }

    public LocalDate getExpectedPregnancyDate() {
        return expectedPregnancyDate;
    }

    public void setExpectedPregnancyDate(LocalDate pregnancyExpectedBirthDate) {
        this.expectedPregnancyDate = pregnancyExpectedBirthDate;
    }

    public String getStudHorse() {
        return studHorse;
    }

    public void setStudHorse(String studHorse) {
        this.studHorse = studHorse;
    }

    public List<LocalDate> getPlannedDates() {
        return plannedDates.keySet().stream().toList();
    }

    public void addPlannedDate(LocalDate date) {
        if (plannedDates.containsKey(date)) {
            throw new DateAlreadyAddedException("Can't add more than 1 planned meeting per day.");
        }
        plannedDates.put(date, false);
    }

    public void removePlannedDate(LocalDate date) {
        if (!plannedDates.containsKey(date)) {
            throw new DateNotFoundException("This day has no planned meeting yet");
        }
        plannedDates.remove(date);
    }

    public void setPregnantDate(LocalDate date) {
        if (!plannedDates.containsKey(date)) {
            throw new DateNotFoundException("Could not find the date! are you sure?");
        }
        plannedDates.put(date, true);
    }

    public long getDaysBetweenNowAndPregnancyDate() {
        if (isPregnant()) {
            return Math.abs(getExpectedPregnancyDate().toEpochDay() - LocalDate.now().toEpochDay());
        }
        return -1;
    }

    public long getDaysThatHorseIsPregnant() {
        if (isPregnant()) {
            return Math.abs(LocalDate.now().toEpochDay() - getDatePregnant().toEpochDay());
        }
        return -1;
    }
}
