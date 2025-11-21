package pe.ask.library.model.publisher;

import java.util.UUID;

public class PublisherBuilder {
    private UUID id;
    private String name;
    private String address;
    private String country;

    public PublisherBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public PublisherBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public PublisherBuilder withAddress(String address) {
        this.address = address;
        return this;
    }

    public PublisherBuilder withCountry(String country) {
        this.country = country;
        return this;
    }

    public Publisher build() {
        return new Publisher(id, name, address, country);
    }
}
