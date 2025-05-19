package be.pxl.researchproject.builder;

import be.pxl.researchproject.domain.*;
import java.time.LocalDate;
import java.sql.Date;
import java.util.*;

public class FoalBuilder {

    private String name = "Foal";
    private LocalDate birthDate = LocalDate.now();
    private Map<Date, String> dewormingSchedule = new HashMap<>();
    private Horse motherHorse;

    private FoalBuilder() {
    }

    public static FoalBuilder aFoal() {
        return new FoalBuilder();
    }

    public FoalBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public FoalBuilder withBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public FoalBuilder withDewormingSchedule(Map<Date, String> dewormingSchedule) {
        this.dewormingSchedule = dewormingSchedule;
        return this;
    }

    public FoalBuilder withMotherHorse(Horse motherHorse) {
        this.motherHorse = motherHorse;
        return this;
    }

    public Foal build() {
        Foal foal = new Foal(name, birthDate, motherHorse);
        foal.setDewormingSchedule(dewormingSchedule);
        return foal;
    }
}
