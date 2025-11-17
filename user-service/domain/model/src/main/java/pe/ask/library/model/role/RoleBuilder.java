package pe.ask.library.model.role;

public class RoleBuilder {

    private String id;
    private String name;
    private String description;

    public RoleBuilder() { }

    public RoleBuilder id(String id) {
        this.id = id;
        return this;
    }

    public RoleBuilder name(String name) {
        this.name = name;
        return this;
    }

    public RoleBuilder description(String description) {
        this.description = description;
        return this;
    }

    public Role build() {
        return new Role(id, name, description);
    }
}
