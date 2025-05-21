package be.pxl.researchproject.service.user.impl;

import java.util.List;
import org.springframework.security.core.userdetails.*;
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
import be.pxl.researchproject.service.user.UserService;

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
        return userRepository.findAll().stream()
                .map(user -> new UserDTO(
                    user.getId(), 
                    user.getUsername(), 
                    user.getEmail(), 
                    user.getRole(),
                    user.isEnabled() ? "true" : "false"))
                .toList();
    }

    @Override
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> new UserDTO(
                    user.getId(), 
                    user.getUsername(), 
                    user.getEmail(), 
                    user.getRole(),
                    user.isEnabled() ? "true" : "false"))
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public Long createUser(CreateUserRequest request) {
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email().toLowerCase());
        user.setRole(Role.valueOf(request.role()));
        user.setEnabled(request.enabled());
        user.setPassword(passwordEncoder.encode(request.password()));
        return userRepository.save(user).getId();
    }

    @Override
    public UserDTO updateUser(Long id, UpdateUserRequest request) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(request.username());
                    user.setEmail(request.email());
                    user.setPassword(passwordEncoder.encode(request.password()));
                    user.setRole(Role.valueOf(request.role()));
                    user.setEnabled(request.enabled());
                    return new UserDTO(
                        user.getId(), 
                        user.getUsername(), 
                        user.getEmail(), 
                        user.getRole(),
                        user.isEnabled() ? "true" : "false");
                })
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public boolean deleteUser(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        return userRepository.findByUsername(usernameOrEmail)
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