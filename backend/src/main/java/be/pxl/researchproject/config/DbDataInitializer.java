package be.pxl.researchproject.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import be.pxl.researchproject.api.request.CreateUserRequest;
import be.pxl.researchproject.domain.*;
import be.pxl.researchproject.service.UserService;

@Component
public class DbDataInitializer implements CommandLineRunner {

    private final UserService userService;

    public DbDataInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {

        // Create some users.
        User u1 = new User();
        u1.setId(1L);
        u1.setUsername("Hanne Geusens");
        u1.setEmail("geusens_hanne@hotmail.com");
        u1.setPassword("123456");
        u1.setRole(Role.ADMIN);
        u1.setEnabled(true);

        User u2 = new User();
        u2.setId(2L);
        u2.setUsername("Eric Cartman");
        u2.setEmail("eric.cartman@gmail.com");
        u2.setPassword("654321");
        u2.setRole(Role.USER);
        u2.setEnabled(true);

        User u3 = new User();
        u3.setId(3L);
        u3.setUsername("AON3");
        u3.setEmail("team.aon03@gmail.com");
        u3.setPassword("qwerty");
        u3.setRole(Role.USER);
        u3.setEnabled(true);

        // User u3 = new User();
        // u3.setId(3L);
        // u3.setUsername("Tom Bot");
        // u3.setEmail("tom.bot@gmail.com");
        // u3.setPassword("qwerty");
        // u3.setRole(Role.USER);
        // u3.setEnabled(false);

        // Save users to the database.
        /**CreateUserRequest createUserRequest1 = new CreateUserRequest(u1.getUsername(), u1.getEmail(), u1.getPassword(),
                u1.getRole().name(),
                u1.isEnabled());
        CreateUserRequest createUserRequest2 = new CreateUserRequest(u2.getUsername(), u2.getEmail(), u2.getPassword(),
                u2.getRole().name(),
                u2.isEnabled());
        CreateUserRequest createUserRequest3 = new CreateUserRequest(u3.getUsername(), u3.getEmail(), u3.getPassword(),
                u3.getRole().name(),
                u3.isEnabled());

        userService.createUser(createUserRequest1);
        userService.createUser(createUserRequest2);
        userService.createUser(createUserRequest3);**/

        createUserIfNotExists(u1);
        createUserIfNotExists(u2);
        createUserIfNotExists(u3);
    }

    private void createUserIfNotExists(User user) {
        if (!userService.userExistsByUsername(user.getUsername()) && !userService.userExistsByEmail(user.getEmail())) {
            CreateUserRequest createUserRequest = new CreateUserRequest(user.getUsername(), user.getEmail(), user.getPassword(),
                    user.getRole().name(), user.isEnabled());
            userService.createUser(createUserRequest);
        }
    }
}
