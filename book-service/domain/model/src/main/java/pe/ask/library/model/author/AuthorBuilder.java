package pe.ask.library.model.author;

import java.util.UUID;

public class AuthorBuilder {
    private UUID id;
    private String firstName;
    private String lastName;
    private String pseudonym;
    private String nationality;

    public AuthorBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public AuthorBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public AuthorBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public AuthorBuilder withPseudonym(String pseudonym) {
        this.pseudonym = pseudonym;
        return this;
    }

    public AuthorBuilder withNationality(String nationality) {
        this.nationality = nationality;
        return this;
    }

    public Author build() {
        return new Author(id, firstName, lastName, pseudonym, nationality);
    }
}
