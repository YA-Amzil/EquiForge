package be.pxl.researchproject.service.user;

import java.util.List;
import be.pxl.researchproject.api.request.*;
import be.pxl.researchproject.api.response.UserDTO;

public interface UserService {
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id);
    Long createUser(CreateUserRequest createUserRequest);
    UserDTO updateUser(Long id, UpdateUserRequest updateUserRequest);
    boolean deleteUser(Long id);
    boolean userExistsByEmail(String email);
    boolean userExistsByUsername(String username);
}