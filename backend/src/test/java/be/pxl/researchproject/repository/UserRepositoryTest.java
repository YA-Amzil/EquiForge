package be.pxl.researchproject.repository;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import be.pxl.researchproject.builder.UserBuilder;
import be.pxl.researchproject.domain.*;
import jakarta.persistence.*;

@DataJpaTest
@Transactional
@Rollback
class UserRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user1 = UserBuilder.anUser().withUsername("testuser1").withEmail("test1@example.com").build();
    private User user2 = UserBuilder.anUser().withUsername("testuser2").withEmail("test2@example.com").build();

    @BeforeEach
    void init() {
        userRepository.saveAll(Arrays.asList(user1, user2));
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testFindByIdValidIdReturnsUser() {
        Optional<User> foundUser = userRepository.findById(user1.getId());
        assertTrue(foundUser.isPresent());
        assertEquals(user1.getUsername(), foundUser.get().getUsername());
        assertEquals(user1.getEmail(), foundUser.get().getEmail());
    }

    @Test
    void testFindAllReturnsAllUsers() {
        List<User> allUsers = userRepository.findAll();
        assertEquals(2, allUsers.size());
        assertTrue(allUsers.stream().anyMatch(user -> user.getUsername().equals("testuser1")));
        assertTrue(allUsers.stream().anyMatch(user -> user.getUsername().equals("testuser2")));
    }

    @Test
    void testSaveNewUserSavesUser() {
        User newUser = UserBuilder.anUser()
                .withUsername("NewUser")
                .withEmail("newuser@example.com")
                .withPassword("newpassword")
                .withRole(Role.USER)
                .withEnabled(true)
                .build();

        User savedUser = userRepository.save(newUser);
        assertNotNull(savedUser.getId());
        assertEquals(newUser.getUsername(), savedUser.getUsername());
        assertEquals(newUser.getEmail(), savedUser.getEmail());
        assertEquals(newUser.getPassword(), savedUser.getPassword());
        assertEquals(newUser.getRole(), savedUser.getRole());
        assertEquals(newUser.isEnabled(), savedUser.isEnabled());
    }

    @Test
    void testSaveExistingUserUpdatesUser() {
        user1.setUsername("UpdatedUser");
        user1.setEmail("updateduser@example.com");
        user1.setPassword("updatedpassword");
        user1.setRole(Role.ADMIN);
        user1.setEnabled(false);

        User updatedUser = userRepository.save(user1);
        assertEquals(user1.getId(), updatedUser.getId());
        assertEquals(user1.getUsername(), updatedUser.getUsername());
        assertEquals(user1.getEmail(), updatedUser.getEmail());
        assertEquals(user1.getPassword(), updatedUser.getPassword());
        assertEquals(user1.getRole(), updatedUser.getRole());
        assertEquals(user1.isEnabled(), updatedUser.isEnabled());
    }

    @Test
    void testDeleteExistingUserDeletesUser() {
        userRepository.deleteById(user1.getId());
        Optional<User> deletedUser = userRepository.findById(user1.getId());
        assertTrue(deletedUser.isEmpty());
    }

    @Test
    void testFindByUsernameValidUsernameReturnsUser() {
        Optional<User> foundUser = userRepository.findByUsername(user1.getUsername());
        assertTrue(foundUser.isPresent());
        assertEquals(user1.getId(), foundUser.get().getId());
        assertEquals(user1.getEmail(), foundUser.get().getEmail());
    }

    @Test
    void testFindByUsernameInvalidUsernameReturnsEmptyOptional() {
        Optional<User> foundUser = userRepository.findByUsername("nonexistentusername");
        assertTrue(foundUser.isEmpty());
    }
}
