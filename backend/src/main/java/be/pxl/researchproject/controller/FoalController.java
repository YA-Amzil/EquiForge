package be.pxl.researchproject.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import be.pxl.researchproject.api.request.*;
import be.pxl.researchproject.api.response.FoalDTO;
import be.pxl.researchproject.service.FoalService;

@RestController
@RequestMapping(path = "/foals")
public class FoalController {

    private final FoalService foalService;

    @Autowired
    public FoalController(FoalService foalService) {
        this.foalService = foalService;
    }

    @GetMapping()
    public ResponseEntity<List<FoalDTO>> getAllFoals() {
        return ResponseEntity.ok(foalService.getAllFoals());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<FoalDTO> getFoalById(@PathVariable Long id) {
        return new ResponseEntity<FoalDTO>(foalService.getFoalById(id), HttpStatus.OK);
    }

    @PostMapping(path = "/create-new")
    public ResponseEntity<Long> createFoal(@RequestBody CreateFoalRequest createFoalRequest) {

        return new ResponseEntity<Long>(foalService.createFoal(createFoalRequest), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}/update")
    public ResponseEntity<FoalDTO> updateFoal(@PathVariable Long id,
                                              @RequestBody UpdateFoalRequest updateFoalRequest) {
        return new ResponseEntity<FoalDTO>(foalService.updateFoal(id, updateFoalRequest), HttpStatus.ACCEPTED);
    }

    @DeleteMapping(path = "/{id}/delete")
    public ResponseEntity<Void> deleteFoal(@PathVariable Long id) {
        boolean deleted = foalService.deleteFoal(id);
        return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/{id}/schedule")
    public String getFoalScheduleByFoalId(@PathVariable long id) {
        return foalService.getSchedule(id);
    }

    @PutMapping(path = "/{id}/schedule/update")
    public ResponseEntity<Void> saveFoalSchedule(@PathVariable long id,
                                               @RequestBody @Valid UpdateFoalScheduleRequest updateFoalScheduleRequest) {
        foalService.updateFoalSchedule(id, updateFoalScheduleRequest);
        return ResponseEntity.ok().build();
    }
}
