package pe.ask.library.model.book;

import pe.ask.library.model.author.Author;
import pe.ask.library.model.category.Category;
import pe.ask.library.model.publisher.Publisher;

public class BookCompleteBuilder {
    private Book book;
    private Author author;
    private Category category;
    private Publisher publisher;

    public BookCompleteBuilder withBook(Book book) {
        this.book = book;
        return this;
    }

    public BookCompleteBuilder withAuthor(Author author) {
        this.author = author;
        return this;
    }

    public BookCompleteBuilder withCategory(Category category) {
        this.category = category;
        return this;
    }

    public BookCompleteBuilder withPublisher(Publisher publisher) {
        this.publisher = publisher;
        return this;
    }

    public BookComplete build() {
        return new BookComplete(book, author, category, publisher);
    }
}
