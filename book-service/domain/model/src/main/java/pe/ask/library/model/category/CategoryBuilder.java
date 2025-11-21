package pe.ask.library.model.category;

import java.util.UUID;

public class CategoryBuilder {
    private UUID id;
    private String name;
    private String description;

    public CategoryBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public CategoryBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CategoryBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public Category build() {
        return new Category(id, name, description);
    }
}
