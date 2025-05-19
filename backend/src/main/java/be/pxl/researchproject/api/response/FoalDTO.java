package be.pxl.researchproject.api.response;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;

import be.pxl.researchproject.domain.Foal;
import com.fasterxml.jackson.annotation.JsonFormat;

public record FoalDTO(
        Long id,
        String name,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate birthDate,
        Map<Date, String> dewormingSchedule)
{
    public FoalDTO(Foal foal) {
        this(foal.getId(), foal.getName(), foal.getBirthDate(), foal.getDewormingSchedule());
    }
}
