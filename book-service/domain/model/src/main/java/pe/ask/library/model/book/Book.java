package pe.ask.library.model.book;

import java.util.UUID;

public class Book {
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

    public Book() {}

    public Book(UUID id, String title, String gender, String summary, int numberOfPages, String language, Format format, int stock, UUID publisherId, UUID categoryId, UUID authorId) {
        this.id = id;
        this.title = title;
        this.gender = gender;
        this.summary = summary;
        this.numberOfPages = numberOfPages;
        this.language = language;
        this.format = format;
        this.publisherId = publisherId;
        this.categoryId = categoryId;
        this.authorId = authorId;
        this.stock = stock;
    }

    public static BookBuilder builder() {
        return new BookBuilder();
    }

    public BookBuilder toBuilder() {
        return new BookBuilder()
                .withId(this.id)
                .withTitle(this.title)
                .withGender(this.gender)
                .withSummary(this.summary)
                .withNumberOfPages(this.numberOfPages)
                .withLanguage(this.language)
                .withFormat(this.format)
                .withPublisherId(this.publisherId)
                .withCategoryId(this.categoryId)
                .withAuthorId(this.authorId);
    }

    public UUID getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getGender() {
        return gender;
    }
    public String getSummary() {
        return summary;
    }
    public int getNumberOfPages() {
        return numberOfPages;
    }
    public String getLanguage() {
        return language;
    }
    public Format getFormat() {
        return format;
    }
    public int getStock() {
        return stock;
    }
    public UUID getPublisherId() {
        return publisherId;
    }
    public UUID getCategoryId() {
        return categoryId;
    }
    public UUID getAuthorId() {
        return authorId;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public void setFormat(Format format) {
        this.format = format;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public void setPublisherId(UUID publisherId) {
        this.publisherId = publisherId;
    }
    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }
    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }
}
