package pe.ask.library.model.book;

import pe.ask.library.model.author.Author;
import pe.ask.library.model.category.Category;
import pe.ask.library.model.publisher.Publisher;

public class BookComplete {
    private Book book;
    private Author author;
    private Category category;
    private Publisher publisher;

    public BookComplete() {}

    public BookComplete(Book book, Author author, Category category, Publisher publisher) {
        this.book = book;
        this.author = author;
        this.category = category;
        this.publisher = publisher;
    }

    public static BookCompleteBuilder builder() {
        return new BookCompleteBuilder();
    }

    public BookCompleteBuilder toBuilder() {
        return new BookCompleteBuilder()
                .withBook(this.book)
                .withAuthor(this.author)
                .withCategory(this.category)
                .withPublisher(this.publisher);
    }

    public Book getBook() {
        return book;
    }

    public Author getAuthor() {
        return author;
    }

    public Category getCategory() {
        return category;
    }

    public Publisher getPublisher() {
        return publisher;
    }
}
