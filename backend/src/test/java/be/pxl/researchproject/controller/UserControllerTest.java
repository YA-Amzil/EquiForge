package be.pxl.researchproject.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import be.pxl.researchproject.api.request.*;
import be.pxl.researchproject.api.response.UserDTO;
import be.pxl.researchproject.domain.Role;
import be.pxl.researchproject.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private UserService userService;

        @Test
        void testGetAllUsersReturnsListOfUsers() throws Exception {
                List<UserDTO> users = List.of(
                                new UserDTO(1L, "Alice", "alice@example.com", Role.USER, "true"),
                                new UserDTO(2L, "Bob", "bob@example.com", Role.ADMIN, "true"));
                when(userService.getAllUsers()).thenReturn(users);
                mockMvc.perform(get("/users"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$[0].id").value(1))
                                .andExpect(jsonPath("$[0].username").value("Alice"))
                                .andExpect(jsonPath("$[0].email").value("alice@example.com"))
                                .andExpect(jsonPath("$[0].role").value("USER"))
                                .andExpect(jsonPath("$[0].enabled").value("true"))
                                .andExpect(jsonPath("$[1].id").value(2))
                                .andExpect(jsonPath("$[1].username").value("Bob"))
                                .andExpect(jsonPath("$[1].email").value("bob@example.com"))
                                .andExpect(jsonPath("$[1].role").value("ADMIN"))
                                .andExpect(jsonPath("$[1].enabled").value("true"));
                verify(userService).getAllUsers();
        }

        @Test
        void testGetUserByIdReturnsUserWithGivenId() throws Exception {
                UserDTO user = new UserDTO(1L, "Alice", "alice@example.com", Role.USER, "true");
                when(userService.getUserById(1L)).thenReturn(user);
                mockMvc.perform(get("/users/1"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.username").value("Alice"))
                                .andExpect(jsonPath("$.email").value("alice@example.com"))
                                .andExpect(jsonPath("$.role").value("USER"))
                                .andExpect(jsonPath("$.enabled").value("true"));
                verify(userService).getUserById(1L);
        }

        @Test
        void testCreateUserReturnsUserId() throws Exception {
                CreateUserRequest createUserRequest = new CreateUserRequest("Alice", "alice@example.com", "password123",
                                Role.USER.name(), true);
                Long userId = 1L;
                when(userService.createUser(createUserRequest)).thenReturn(userId);
                mockMvc.perform(post("/users/create-new")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createUserRequest)))
                                .andExpect(status().isCreated());

                verify(userService).createUser(createUserRequest);
        }

        @Test
        void testUpdateUserReturnsUpdatedUserAccepted() throws Exception {
                Long userId = 1L;
                UpdateUserRequest updateUserRequest = new UpdateUserRequest("Timmy", "timmy@example.com", "654321",
                                Role.ADMIN.name(), true);
                UserDTO updatedUser = new UserDTO(userId, "Timmy", "timmy@example.com", Role.ADMIN, "true");
                when(userService.updateUser(userId, updateUserRequest)).thenReturn(updatedUser);
                mockMvc.perform(put("/users/1/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateUserRequest)))
                                .andExpect(status().isAccepted())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.id").value(userId))
                                .andExpect(jsonPath("$.username").value("Timmy"))
                                .andExpect(jsonPath("$.email").value("timmy@example.com"))
                                .andExpect(jsonPath("$.role").value("ADMIN"))
                                .andExpect(jsonPath("$.enabled").value("true"));
                verify(userService).updateUser(userId, updateUserRequest);
        }

        @Test
        void testDeleteUserReturnsNoContent() throws Exception {
                Long userId = 1L;
                when(userService.deleteUser(userId)).thenReturn(true);
                mockMvc.perform(delete("/users/1/delete"))
                                .andExpect(status().isNoContent());
                verify(userService).deleteUser(userId);
        }

        @Test
        void testDeleteUserReturnsNotFound() throws Exception {
                Long userId = 1L;
                when(userService.deleteUser(userId)).thenReturn(false);
                mockMvc.perform(delete("/users/1/delete"))
                                .andExpect(status().isNotFound());
                verify(userService).deleteUser(userId);
        }
}