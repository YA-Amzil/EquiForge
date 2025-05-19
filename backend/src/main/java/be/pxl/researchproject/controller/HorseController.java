package be.pxl.researchproject.controller;

import be.pxl.researchproject.api.request.*;
import be.pxl.researchproject.api.response.*;
import be.pxl.researchproject.domain.Foal;
import be.pxl.researchproject.service.HorseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/horses")
public class HorseController {

    private final HorseService horseService;

    @Autowired
    public HorseController(HorseService horseService) {
        this.horseService = horseService;
    }

    @GetMapping
    public List<HorseListItemDTO> getHorses() {
        return horseService.getHorses();
    }

    @PostMapping(path = "/create-new")
    public ResponseEntity<Void> createHorse(@RequestBody @Valid CreateHorseRequest createHorseRequest) {
        Long id = horseService.createHorse(createHorseRequest);
        return ResponseEntity.created(URI.create("/horses/" + id)).build();
    }

    @PostMapping(path = "/{id}/fertilization")
    public ResponseEntity<Void> createFertilizationDate(@PathVariable Long id,
            @RequestBody @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy") LocalDate date) {
        horseService.createFertilizationDate(id, date);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(path = "/{id}")
    public HorseDTO getHorseById(@PathVariable Long id) {
        return horseService.getHorseById(id);
    }

    @PutMapping(path = "/{id}/update")
    public ResponseEntity<Void> updateHorse(@PathVariable Long id, @RequestBody UpdateHorseRequest updateHorseRequest) {
        horseService.updateHorse(id, updateHorseRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/{id}/updatehorse")
    public HorseUpdateDTO getUpdatableHorse(@PathVariable Long id) {
        return horseService.getUpdatableHorseById(id);
    }

    @DeleteMapping(path = "/{id}/delete")
    public ResponseEntity<Void> deleteHorse(@PathVariable Long id) {
        horseService.deleteHorseById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/{id}/plannedDates")
    public List<LocalDate> getPlannedDatesbyHorseId(@PathVariable long id) {
        return horseService.getPlannedDatesByHorseId(id);
    }

    @DeleteMapping(path = "/{id}/plannedDates")
    public ResponseEntity<Void> deletePlannedDate(@PathVariable long id,
          @RequestBody @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy") String date) {
        System.out.println(date);
        String dateString = date.substring(1, date.length() - 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");

        try {
            LocalDate localDate = LocalDate.parse(dateString, formatter);

            System.out.println("Parsed LocalDate: " + localDate);
            horseService.deletePlannedDateForHorse(id, localDate);
            System.out.println("Resterende horses: " + horseService.getPlannedDatesByHorseId(id));
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing the date: " + e.getMessage());
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/{id}/plannedDates")
    public ResponseEntity<Void> createFertilizationDate(@PathVariable Long id,
            @RequestBody @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy") String date) {
        String dateString = date.substring(1, date.length() - 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate localDate = LocalDate.parse(dateString, formatter);

            System.out.println("Parsed LocalDate: " + localDate);
            horseService.createFertilizationDate(id, localDate);
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing the date: " + e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(path = "/{id}/setPregnancy")
    public ResponseEntity<Void> setPregnancyDate(@PathVariable Long id,
            @RequestBody @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy") String date) {
        String dateString = date.substring(1, date.length() - 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate localDate = LocalDate.parse(dateString, formatter);

            System.out.println("Parsed LocalDate: " + localDate);
            horseService.setPregnancyDate(id, localDate);
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing the date: " + e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(path = "/{id}/setPregnancy")
    public LocalDate getPregnancyDate(@PathVariable Long id) {
        return horseService.getPregnancyDate(id);
    }


    @GetMapping(path = "/{id}/diaries")
    public Map<String, String> getBirthDiariesByHorseId(@PathVariable long id) {
        return horseService.getBirthDiariesByHorseId(id);
    }

    @PutMapping(path = "/{id}/diaries")
    public ResponseEntity<Void> saveHorseDiary(@PathVariable long id,
            @RequestBody @Valid UpdateHorseDiaryRequest updateHorseDiaryRequest) {
        horseService.updateHorseDiary(id, updateHorseDiaryRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/{id}/diaries")
    public ResponseEntity<Void> createHorseDiary(@PathVariable long id,
            @RequestBody @Valid CreateHorseDiaryRequest createHorseDiaryRequest) {
        horseService.createHorseDiary(id, createHorseDiaryRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/{id}/foals")
    public List<Foal> getFoalsByHorseId(@PathVariable long id){
        return horseService.getFoalsByHorseId(id);
    }
}
