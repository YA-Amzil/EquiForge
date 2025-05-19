package be.pxl.researchproject.domain;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class UserDomainTest {

    private User setUser;
    private User setAdmin;

    @BeforeEach
    void setup() {
        setUser = new User("testuser", "test@example.com", "password123", Role.USER, true);
        setAdmin = new User("adminuser", "admin@example.com", "admin123", Role.ADMIN, true);
    }

    @Test
    void testUserGettersAndSetters() {
        assertEquals("testuser", setUser.getUsername());
        assertEquals("test@example.com", setUser.getEmail());
        assertEquals("password123", setUser.getPassword());
        assertEquals(Role.USER, setUser.getRole());
        assertTrue(setUser.isEnabled());
    }

    @Test
    void testAdminGettersAndSetters() {
        assertEquals("adminuser", setAdmin.getUsername());
        assertEquals("admin@example.com", setAdmin.getEmail());
        assertEquals("admin123", setAdmin.getPassword());
        assertEquals(Role.ADMIN, setAdmin.getRole());
        assertTrue(setAdmin.isEnabled());
    }

    @Test
    void testToString() {
        String expectedUserToString = "User{id=null, username='testuser', email='test@example.com', password='password123', role=USER, enabled=true}";
        String expectedAdminToString = "User{id=null, username='adminuser', email='admin@example.com', password='admin123', role=ADMIN, enabled=true}";
        assertEquals(expectedUserToString, setUser.toString());
        assertEquals(expectedAdminToString, setAdmin.toString());

    }
}
