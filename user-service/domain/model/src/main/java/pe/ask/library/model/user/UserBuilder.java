package pe.ask.library.model.user;

import java.time.LocalDateTime;

public class UserBuilder {

    private String id;
    private String name;
    private String lastName;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String roleId;

    public UserBuilder() { }

    public UserBuilder id(String id) {
        this.id = id;
        return this;
    }

    public UserBuilder name(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder password(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public UserBuilder updatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public UserBuilder roleId(String roleId) {
        this.roleId = roleId;
        return this;
    }

    public User build() {
        return new User(
                id,
                name,
                lastName,
                email,
                password,
                createdAt,
                updatedAt,
                roleId
        );
    }
}
