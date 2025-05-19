package be.pxl.researchproject.service.Impl;

import java.util.List;
import org.springframework.security.core.userdetails.*;
import java.util.concurrent.atomic.AtomicBoolean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import be.pxl.researchproject.api.request.*;
import be.pxl.researchproject.api.response.UserDTO;
import be.pxl.researchproject.domain.Role;
import be.pxl.researchproject.domain.User;
import be.pxl.researchproject.exception.UserNotFoundException;
import be.pxl.researchproject.repository.UserRepository;
import be.pxl.researchproject.security.UserPrincipal;
import be.pxl.researchproject.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(user -> new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole(),
                        user.isEnabled() ? "true" : "false"))
                .toList();
    }

    @Override
    public UserDTO getUserById(Long id) {
        return userRepository
                .findById(id)
                .map(user -> new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole(),
                        user.isEnabled() ? "true" : "false"))
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public UserDTO updateUser(Long id, UpdateUserRequest updateUserRequest) {
        return userRepository
                .findById(id)
                .map(user -> {
                    user.setUsername(updateUserRequest.username());
                    user.setEmail(updateUserRequest.email());
                    user.setPassword(passwordEncoder.encode(updateUserRequest.password()));
                    user.setRole(Role.valueOf(updateUserRequest.role()));
                    user.setEnabled(updateUserRequest.enabled());
                    return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole(),
                            user.isEnabled() ? "true" : "false");
                })
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public boolean deleteUser(Long id) {
        AtomicBoolean found = new AtomicBoolean(false);

        userRepository
                .findById(id)
                .ifPresent(user -> {
                    userRepository.delete(user);
                    found.set(true);
                });
        return found.get();
    }

    @Override
    public Long createUser(CreateUserRequest createUserRequest) {
        User user = new User();
        user.setUsername(createUserRequest.username());
        user.setEmail(createUserRequest.email().toLowerCase());
        user.setRole(Role.valueOf(createUserRequest.role()));
        user.setEnabled(createUserRequest.enabled());
        user.setPassword(passwordEncoder.encode(createUserRequest.password()));
        return userRepository.save(user).getId();
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(usernameOrEmail)
                .or(() -> userRepository.findByEmail(usernameOrEmail))
                .map(UserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + usernameOrEmail));

    }

    @Override
    public boolean userExistsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public boolean userExistsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
