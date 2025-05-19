package be.pxl.researchproject.builder;

import java.time.LocalDate;
import be.pxl.researchproject.domain.Horse;

public class HorseBuilder {

    private String name = "Nicky";
    private LocalDate birthDate = LocalDate.now();
    private double height = 150;
    private boolean isPregnant = false;
    private String studHorse = "Mickey";
    private LocalDate datePregnant = LocalDate.now();

    public HorseBuilder() {

    }

    public HorseBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public HorseBuilder withBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public HorseBuilder withHeight(double height) {
        this.height = height;
        return this;
    }

    public HorseBuilder isPregnant(boolean isPregnant) {
        this.isPregnant = isPregnant;
        return this;
    }

    public HorseBuilder withStudHorse(String studHorse) {
        this.studHorse = studHorse;
        return this;
    }

    public HorseBuilder withDatePregnant(LocalDate datePregnant) {
        this.datePregnant = datePregnant;
        return this;
    }

    public Horse build() {
        return new Horse(name, birthDate, height, isPregnant, studHorse, datePregnant);
    }
}
