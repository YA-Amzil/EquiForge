package be.pxl.researchproject.service;

import java.util.List;
import be.pxl.researchproject.api.request.*;
import be.pxl.researchproject.api.response.FoalDTO;
import io.swagger.v3.oas.models.links.Link;

public interface FoalService {
    Long createFoal(CreateFoalRequest createFoalRequest);

    FoalDTO getFoalById(Long id);

    List<FoalDTO> getAllFoals();

    FoalDTO updateFoal(Long id, UpdateFoalRequest updateFoalRequest);

    boolean deleteFoal(Long id);

    String getSchedule(Long id);

    void updateFoalSchedule(long id, UpdateFoalScheduleRequest updateFoalscheduleRequest);
}
