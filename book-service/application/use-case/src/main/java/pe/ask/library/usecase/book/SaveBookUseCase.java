package pe.ask.library.usecase.book;

import pe.ask.library.model.book.Book;
import pe.ask.library.port.in.usecase.book.ISaveBookUseCase;
import pe.ask.library.port.out.kafka.KafkaSender;
import pe.ask.library.port.out.logger.Logger;
import pe.ask.library.port.out.persistence.IBookRepository;
import pe.ask.library.usecase.utils.UseCase;
import reactor.core.publisher.Mono;

@UseCase
public class SaveBookUseCase implements ISaveBookUseCase {

    private final IBookRepository repository;

    public SaveBookUseCase(IBookRepository repository) {
        this.repository = repository;
    }

    @Logger
    @Override
    @KafkaSender(topic = "audit-log")
    public Mono<Book> saveBook(Book book) {
        return repository.saveBook(book);
    }
}
