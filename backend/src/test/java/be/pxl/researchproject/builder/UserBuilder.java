package be.pxl.researchproject.builder;

import be.pxl.researchproject.domain.*;

public final class UserBuilder {

    private String username = "testuser";
    private String email = "test@example.com";
    private String password = "test123";
    private Role role = Role.USER;
    private boolean enabled = true;

    private UserBuilder() {
    }

    public static UserBuilder anUser() {
        return new UserBuilder();
    }

    public UserBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder withRole(Role role) {
        this.role = role;
        return this;
    }

    public UserBuilder withEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public User build() {
        return new User(username, email, password, role, enabled);
    }
}
