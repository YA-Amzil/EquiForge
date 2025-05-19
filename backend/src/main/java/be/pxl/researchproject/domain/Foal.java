package be.pxl.researchproject.domain;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import be.pxl.researchproject.exception.InvalidDewormingScheduleException;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "foals")
public class Foal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDate birthDate;
    private String schedule;
    @ElementCollection(fetch = FetchType.EAGER)
    private Map<Date, String> dewormingSchedule = new HashMap<>();
    @ManyToOne
    @JsonBackReference
    private Horse motherHorse;

    public Foal() {
        // JPA only
    }

    public Foal(String name, LocalDate birthDate, Horse motherHorse) {
        setName(name);
        setBirthDate(birthDate);
        System.out.println(birthDate);
        setMotherHorse(motherHorse);

    }

    public void calculatedewormingSchedule() {

        if (this.getBirthDate() != null && this.getName() != null) {
            List<String> medicationList = Arrays.asList("Panacur", "Pyrantel", "Eraquell or Furexcel", "Panacur");

            LocalDate nextDewormingDate = this.getBirthDate().plusDays(1);
            for (String medication : medicationList) {
                this.getDewormingSchedule().put(Date.valueOf(nextDewormingDate), medication);
                nextDewormingDate = nextDewormingDate.plusMonths(1);
            }
            makeSchedule();
        } else {
            throw new InvalidDewormingScheduleException("Deworming date or name is not set for the foal.");
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

    public int getAge() {
        return calculateAge();
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Map<Date, String> getDewormingSchedule() {
        return dewormingSchedule;
    }

    public void setDewormingSchedule(Map<Date, String> dewormingSchedule) {
        this.dewormingSchedule = dewormingSchedule;
        makeSchedule();
    }

    public Horse getMotherHorse() {
        return motherHorse;
    }

    public void setMotherHorse(Horse motherHorse) {
        this.motherHorse = motherHorse;
        if (motherHorse != null) {
            motherHorse.addFoal(this);
        }
    }

    private int calculateAge() {
        return Math.abs(Period.between(LocalDate.now(), birthDate).getMonths());
    }

    public boolean dewormNeededToday() {
        Date date = new Date(LocalDate.now().getYear(), LocalDate.now().getMonthValue(),
                LocalDate.now().getDayOfMonth());
        return getDewormingSchedule().containsKey(date);
    }

    public void makeSchedule() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<Date, String> entry : this.dewormingSchedule.entrySet()) {
            stringBuilder.append(entry.getKey().toString());
            stringBuilder.append(": ");
            stringBuilder.append(entry.getValue());
            stringBuilder.append(System.lineSeparator());
        }
        this.schedule = stringBuilder.toString();
    }

    public String getSchedule() {
        return schedule;
    }

    public void updateSchedule(String newSchedule) {
        this.schedule = newSchedule;
    }
}