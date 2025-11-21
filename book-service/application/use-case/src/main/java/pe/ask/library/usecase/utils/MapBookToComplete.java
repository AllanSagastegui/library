package pe.ask.library.usecase.utils;

import pe.ask.library.model.book.Book;
import pe.ask.library.model.book.BookComplete;
import pe.ask.library.port.out.persistence.IAuthorRepository;
import pe.ask.library.port.out.persistence.ICategoryRepository;
import pe.ask.library.port.out.persistence.IPublisherRepository;
import reactor.core.publisher.Mono;

@UseCase
public class MapBookToComplete {

    private final IAuthorRepository authorRepository;
    private final ICategoryRepository categoryRepository;
    private final IPublisherRepository publisherRepository;

    public MapBookToComplete(
            IAuthorRepository authorRepository,
            ICategoryRepository categoryRepository,
            IPublisherRepository publisherRepository
    ) {
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.publisherRepository = publisherRepository;
    }

    public Mono<BookComplete> map(Book book) {
        return Mono.zip(
                authorRepository.getAuthorById(book.getAuthorId()),
                categoryRepository.getCategoryById(book.getCategoryId()),
                publisherRepository.getPublisherById(book.getPublisherId())
        ).map(tuple -> BookComplete.builder()
                .withBook(book)
                .withAuthor(tuple.getT1())
                .withCategory(tuple.getT2())
                .withPublisher(tuple.getT3())
                .build()
        );
    }
}
