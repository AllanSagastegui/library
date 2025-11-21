package pe.ask.library.model.author;

import java.util.UUID;

public class Author {
    private UUID id;
    private String firstName;
    private String lastName;
    private String pseudonym;
    private String nationality;

    public Author() {}

    public Author(UUID id, String firstName, String lastName, String pseudonym, String nationality) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pseudonym = pseudonym;
        this.nationality = nationality;
    }

    public static AuthorBuilder builder() {
        return new AuthorBuilder();
    }

    public AuthorBuilder toBuilder() {
        return new AuthorBuilder()
                .withId(this.id)
                .withFirstName(this.firstName)
                .withLastName(this.lastName)
                .withPseudonym(this.pseudonym)
                .withNationality(this.nationality);
    }

    public UUID getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getPseudonym() {
        return pseudonym;
    }
    public String getNationality() {
        return nationality;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setPseudonym(String pseudonym) {
        this.pseudonym = pseudonym;
    }
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
