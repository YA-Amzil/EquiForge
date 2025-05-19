package be.pxl.researchproject.service;

import be.pxl.researchproject.api.request.*;
import be.pxl.researchproject.api.response.UserDTO;
import be.pxl.researchproject.domain.*;
import be.pxl.researchproject.exception.UserNotFoundException;
import be.pxl.researchproject.repository.UserRepository;
import be.pxl.researchproject.service.Impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    List<User> users;

    @BeforeEach
    void setUp() {
        users = List.of(
                new User("Hanne Geusens", "geusens_hanne@hotmail.com", "123456", Role.ADMIN, true),
                new User("Eric Cartman", "eric.cartman@gmail.com", "654321", Role.USER, true),
                new User("Tom Bot", "tom.bot@gmail.com", "qwerty", Role.USER, false));
    }

    @Test
    void testGetAllUsersSuccess() {
        given(userRepository.findAll()).willReturn(users);
        List<UserDTO> allUsers = userService.getAllUsers();
        assertThat(allUsers).hasSize(3);
    }

    @Test
    void testGetUserByIdSuccess() {
        User user = users.get(0);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        UserDTO userDTO = userService.getUserById(1L);
        assertThat(userDTO.id()).isEqualTo(user.getId());
        assertThat(userDTO.username()).isEqualTo(user.getUsername());
        assertThat(userDTO.email()).isEqualTo(user.getEmail());
        assertThat(userDTO.role()).isEqualTo(user.getRole());
        assertThat(userDTO.enabled()).isEqualTo("true");
    }

    @Test
    void testGetUserByIdNotFound() {
        Long userId = 1L;
        given(userRepository.findById(Mockito.any(Long.class))).willReturn(Optional.empty());
        Throwable thrown = catchThrowable(() -> {
            userService.getUserById(1L);
        });
        assertThat(thrown)
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with id " + userId + " not found");
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testCreateUserSuccess() {
        CreateUserRequest createUserRequest = new CreateUserRequest(
                "Alice", "alice@example.com",
                "password123", Role.USER.name(), true);
        User createdUser = new User();
        createdUser.setId(1L);
        createdUser.setPassword(createUserRequest.password());
        given(this.userRepository.save(any(User.class))).willReturn(createdUser);
        given(this.passwordEncoder.encode(createUserRequest.password())).willReturn("password123");
        Long userId = userService.createUser(createUserRequest);
        assertThat(userId).isNotNull();
        verify(userRepository, times(1)).save(any(User.class));
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getUsername()).isEqualTo(createUserRequest.username());
        assertThat(savedUser.getEmail()).isEqualTo(createUserRequest.email());
        assertThat(savedUser.getPassword()).isEqualTo(createUserRequest.password());
        assertThat(savedUser.getRole()).isEqualTo(Role.USER);
        assertThat(savedUser.isEnabled()).isTrue();
    }

    @Test
    void testUpdateUserSuccess() {
        Long userId = 1L;
        User oldUser = users.get(0);
        oldUser.setId(1L);
        UpdateUserRequest updateUserRequest = new UpdateUserRequest(
                "Timmy", "timmy@example.com",
                "654321", "ADMIN", true);
        given(userRepository.findById(userId)).willReturn(Optional.of(oldUser));
        given(passwordEncoder.encode(updateUserRequest.password())).willReturn("654321");
        UserDTO updatedUser = userService.updateUser(userId, updateUserRequest);
        assertThat(updatedUser.id()).isEqualTo(userId);
        assertThat(updatedUser.username()).isEqualTo(updateUserRequest.username());
        assertThat(updatedUser.email()).isEqualTo(updateUserRequest.email());
        assertThat(updatedUser.role()).isEqualTo(Role.ADMIN);
        assertThat(updatedUser.enabled()).isEqualTo("true");
        verify(userRepository, times(1)).findById(userId);
        verify(passwordEncoder, times(1)).encode(updateUserRequest.password());
    }

    @Test
    void testUpdateUserNotFound() {
        Long userId = 1L;
        UpdateUserRequest updateUserRequest = new UpdateUserRequest(
                "Timmy", "timmy@example.com",
                "654321", Role.ADMIN.name(), true);

        given(userRepository.findById(userId)).willReturn(Optional.empty());
        Throwable thrown = assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser(userId, updateUserRequest);
        });
        assertThat(thrown)
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with id " + userId + " not found");
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testLoadUserByUsername_WhenUserNotFound() {
        given(userRepository.findByUsername("username")).willReturn(Optional.empty());
        UsernameNotFoundException exception = org.junit.jupiter.api.Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("username"));
        assertThat(exception.getMessage()).isEqualTo("User not found with username: username");
    }
}
