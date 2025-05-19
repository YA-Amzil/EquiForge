package be.pxl.researchproject.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.time.LocalDate;
import java.util.*;

import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import be.pxl.researchproject.api.request.*;
import be.pxl.researchproject.api.response.FoalDTO;
import be.pxl.researchproject.exception.FoalNotFoundException;
import be.pxl.researchproject.service.FoalService;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class FoalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FoalService foalService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllFoalsReturnsFoalDTOList() throws Exception {
        LocalDate date = LocalDate.now();
        List<FoalDTO> foals = Arrays.asList(
                new FoalDTO(1L, "Foal1", date, null),
                new FoalDTO(2L, "Foal2", date, null));
        when(foalService.getAllFoals()).thenReturn(foals);
        mockMvc.perform(get("/foals"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Foal1")))
                .andExpect(jsonPath("$[0].birthDate", is(date.toString())))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Foal2")))
                .andExpect(jsonPath("$[1].birthDate", is(date.toString())));
    }

    @Test
    void testGetFoalByIdExistingIdReturnsFoalDTO() throws Exception {
        LocalDate date = LocalDate.now();
        FoalDTO foal = new FoalDTO(1L, "Foal1", date, null);
        when(foalService.getFoalById(1L)).thenReturn(foal);
        mockMvc.perform(get("/foals/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Foal1")))
                .andExpect(jsonPath("$.birthDate", is(date.toString())));
    }

    @Test
    void testGetFoalByIdNonExistingIdReturnsNotFound() throws Exception {
        when(foalService.getFoalById(1L)).thenThrow(new FoalNotFoundException("Foal with id 1 not found"));
        mockMvc.perform(get("/foals/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateFoalValidRequestReturnsCreated() throws Exception {
        CreateFoalRequest request = new CreateFoalRequest("Foal", 1L, LocalDate.now());
        String requestBody = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/foals/create-new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateFoalValidRequestReturnsAccepted() throws Exception {
        UpdateFoalRequest request = new UpdateFoalRequest("UpdatedFoal", LocalDate.now());
        String requestBody = objectMapper.writeValueAsString(request);
        mockMvc.perform(put("/foals/1/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isAccepted());
    }

    @Test
    void testDeleteFoalExistingIdReturnsNoContent() throws Exception {
        when(foalService.deleteFoal(1L)).thenReturn(true);
        mockMvc.perform(delete("/foals/1/delete"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteFoalNonExistingIdReturnsNotFound() throws Exception {
        when(foalService.deleteFoal(1L)).thenReturn(false);
        mockMvc.perform(delete("/foals/1/delete"))
                .andExpect(status().isNotFound());
    }
}
