package be.pxl.researchproject.api.response;

import be.pxl.researchproject.domain.*;

public record UserDTO(Long id, String username, String email, Role role, String enabled) {

    public UserDTO(User user) {
        this(user.getId(), user.getUsername(), user.getEmail(), user.getRole(), String.valueOf(user.isEnabled()));
    }

}
