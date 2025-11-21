package pe.ask.library.model.publisher;

import java.util.UUID;

public class Publisher {
    private UUID id;
    private String name;
    private String address;
    private String country;

    public Publisher() {}
    public Publisher(UUID id, String name, String address, String country) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.country = country;
    }

    public static PublisherBuilder builder() {
        return new PublisherBuilder();
    }

    public PublisherBuilder toBuilder() {
        return new PublisherBuilder()
                .withId(this.id)
                .withName(this.name)
                .withAddress(this.address)
                .withCountry(this.country);
    }

    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
    public String getCountry() {
        return country;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setCountry(String country) {
        this.country = country;
    }
}
