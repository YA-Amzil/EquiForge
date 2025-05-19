package be.pxl.researchproject.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.time.LocalDate;
import java.util.*;
import be.pxl.researchproject.api.request.*;
import be.pxl.researchproject.api.response.*;
import be.pxl.researchproject.exception.HorseNotFoundException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import be.pxl.researchproject.service.HorseService;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class HorseControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private HorseService horseService;

        @Test
        void testGetHorsesNoParamsReturnsHorseList() throws Exception {
                List<HorseListItemDTO> horses = Arrays.asList(
                                new HorseListItemDTO(1L, "Horsey", false, 1, 1, "Mike", LocalDate.now()),
                                new HorseListItemDTO(2L, "StoutPaard", true, 1, 1, "Mike", LocalDate.now()));
                when(horseService.getHorses()).thenReturn(horses);
                mockMvc.perform(get("/horses"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$", hasSize(2)))
                                .andExpect(jsonPath("$[0].id", is(1)))
                                .andExpect(jsonPath("$[0].name", is("Horsey")))
                                .andExpect(jsonPath("$[0].pregnant", is(false)))
                                .andExpect(jsonPath("$[0].daysPregnant", is(1)))
                                .andExpect(jsonPath("$[0].expectedPregnancyDays", is(1)))

                                .andExpect(jsonPath("$[1].id", is(2)))
                                .andExpect(jsonPath("$[1].name", is("StoutPaard")))
                                .andExpect(jsonPath("$[1].pregnant", is(true)))
                                .andExpect(jsonPath("$[1].daysPregnant", is(1)))
                                .andExpect(jsonPath("$[1].expectedPregnancyDays", is(1)));
        }

        @Test
        void testgetHorseByIdExistingIdReturnsHorseDTO() throws Exception {
                LocalDate bday = LocalDate.now();
                HorseDTO horseDTO = new HorseDTO("Daisy", bday, 0.5, false, "Mike");
                when(horseService.getHorseById(1L)).thenReturn(horseDTO);
                mockMvc.perform(get("/horses/1"))
                                .andExpect(status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Daisy"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.height").value(0.5))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.isPregnant").value(false))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.studHorse").value("Mike"));
        }

        @Test
        void testFetHorseByIdNonExistingIdReturnsNotFound() throws Exception {
                when(horseService.getHorseById(1L))
                                .thenThrow(new HorseNotFoundException("Horse with id 1 not found"));
                mockMvc.perform(get("/horses/1"))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testUpdateHorseValidRequestReturnsOk() throws Exception {
                LocalDate date = LocalDate.of(2023, 4, 22);
                UpdateHorseRequest updateRequest = new UpdateHorseRequest("Tine", date, 0.5, "Mike", true,
                                LocalDate.of(2023, 4, 22));
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                String requestBody = objectMapper.writeValueAsString(updateRequest);
                mockMvc.perform(put("/horses/1/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andExpect(status().isOk());
        }

        @Test
        void testDeleteHorseExistingIdReturnsNoContent() throws Exception {
                mockMvc.perform(delete("/horses/1/delete"))
                                .andExpect(status().isNoContent());
                verify(horseService).deleteHorseById(1L);
        }

        @Test
        void testdeleteHorseNonExistingIdReturnsNoContent() throws Exception {
                doNothing().when(horseService).deleteHorseById(1L);
                mockMvc.perform(delete("/horses/1/delete"))
                                .andExpect(status().isNoContent());
        }

        @Test
        void testGetBirthDiariesByHorseIdExistingIdReturnsDiariesMap() throws Exception {
                Map<String, String> diaries = new HashMap<>();
                diaries.put("key1", "value1");
                diaries.put("key2", "value2");
                when(horseService.getBirthDiariesByHorseId(1)).thenReturn(diaries);
                mockMvc.perform(get("/horses/1/diaries"))
                                .andExpect(status().isOk());
        }

        @Test
        void testGetBirthDiariesByHorseIdNonExistingId_ReturnsNotFound() throws Exception {
                Long nonExistingId = 2L;
                when(horseService.getBirthDiariesByHorseId(nonExistingId))
                                .thenThrow(new HorseNotFoundException(
                                                "Horse with id " + nonExistingId + " not found"));
                mockMvc.perform(get("/horses/{id}/diaries", nonExistingId))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testSaveHorseDiaryValidRequestReturnsOk() throws Exception {
                UpdateHorseDiaryRequest updateRequest = new UpdateHorseDiaryRequest("2024", "hello");
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonContent = objectMapper.writeValueAsString(updateRequest);
                mockMvc.perform(put("/horses/1/diaries")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonContent))
                                .andExpect(status().isOk());
                verify(horseService).updateHorseDiary(1, updateRequest);
        }

        @Test
        void testsaveHorseDiaryInvalidRequestReturnsBadRequest() throws Exception {
                String invalidJsonContent = "";
                mockMvc.perform(put("/horses/1/diaries")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidJsonContent))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void testCreateHorseDiaryValidRequestReturnsOk() throws Exception {
                CreateHorseDiaryRequest createRequest = new CreateHorseDiaryRequest("2024");
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonContent = objectMapper.writeValueAsString(createRequest);
                mockMvc.perform(post("/horses/1/diaries")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonContent))
                                .andExpect(status().isOk());

                verify(horseService).createHorseDiary(1, createRequest);
        }

        @Test
        void testCreateHorseDiaryInvalidRequestReturnsBadRequest() throws Exception {
                String invalidJsonContent = "";
                mockMvc.perform(post("/horses/1/diaries")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidJsonContent))
                                .andExpect(status().isBadRequest());
        }
}