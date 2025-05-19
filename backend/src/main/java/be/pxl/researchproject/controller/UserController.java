package be.pxl.researchproject.controller;

import be.pxl.researchproject.api.request.*;
import be.pxl.researchproject.api.response.UserDTO;
import be.pxl.researchproject.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping(path = "/create-new")
    public ResponseEntity<Long> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        userService.createUser(createUserRequest);
        return new ResponseEntity<Long>(HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}/update")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id,
                                              @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        return new ResponseEntity<UserDTO>(userService.updateUser(id, updateUserRequest), HttpStatus.ACCEPTED);
    }

    @DeleteMapping(path = "/{id}/delete")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
