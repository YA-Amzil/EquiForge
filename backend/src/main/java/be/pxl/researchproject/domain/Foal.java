package be.pxl.researchproject.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "foals")
public class Foal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDate birthDate;

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<LocalDate, String> dewormingSchedule = new HashMap<>();

    @ManyToOne
    private Horse motherHorse;

    protected Foal() {} // JPA only

    public Foal(String name, LocalDate birthDate, Horse motherHorse) {
        this.name = name;
        this.birthDate = birthDate;
        this.motherHorse = motherHorse;
    }

    public void setDewormingSchedule(Map<LocalDate, String> dewormingSchedule) {
        this.dewormingSchedule = dewormingSchedule;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public LocalDate getBirthDate() { return birthDate; }
    public Map<LocalDate, String> getDewormingSchedule() { return dewormingSchedule; }
    public Horse getMotherHorse() { return motherHorse; }
}
