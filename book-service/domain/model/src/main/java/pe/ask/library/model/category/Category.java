package pe.ask.library.model.category;

import java.util.UUID;

public class Category {
    private UUID id;
    private String name;
    private String description;

    public Category() {}

    public Category(UUID id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static CategoryBuilder builder() {
        return new CategoryBuilder();
    }

    public CategoryBuilder toBuilder() {
        return new CategoryBuilder()
                .withId(this.id)
                .withName(this.name)
                .withDescription(this.description);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
