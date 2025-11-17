package pe.ask.library.model.role;

public class Role {
    private String id;
    private String name;
    private String description;

    public Role() { }

    public Role(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public RoleBuilder builder() {
        return new RoleBuilder();
    }

    public RoleBuilder toBuilder() {
        return new RoleBuilder()
                .id(this.id)
                .name(this.name)
                .description(this.description);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
