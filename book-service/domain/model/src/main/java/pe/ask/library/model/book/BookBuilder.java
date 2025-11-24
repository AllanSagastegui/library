package pe.ask.library.model.book;

import java.util.UUID;

public class BookBuilder {
    private UUID id;
    private String title;
    private String gender;
    private String summary;
    private int numberOfPages;
    private String language;
    private Format format;
    private int stock;
    private UUID publisherId;
    private UUID categoryId;
    private UUID authorId;

    public BookBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public BookBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public BookBuilder withGender(String gender) {
        this.gender = gender;
        return this;
    }

    public BookBuilder withSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public BookBuilder withNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
        return this;
    }

    public BookBuilder withLanguage(String language) {
        this.language = language;
        return this;
    }

    public BookBuilder withFormat(Format format) {
        this.format = format;
        return this;
    }

    public BookBuilder withStock(int stock) {
        this.stock = stock;
        return this;
    }

    public BookBuilder withPublisherId(UUID publisherId) {
        this.publisherId = publisherId;
        return this;
    }

    public BookBuilder withCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public BookBuilder withAuthorId(UUID authorId) {
        this.authorId = authorId;
        return this;
    }

    public Book build() {
        return new Book(id, title, gender, summary, numberOfPages, language, format, stock, publisherId, categoryId, authorId);
    }
}
