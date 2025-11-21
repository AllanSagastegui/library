package pe.ask.library.usecase.book;

import pe.ask.library.model.book.Book;
import pe.ask.library.port.in.usecase.book.IGetBookByIdUseCase;
import pe.ask.library.port.out.kafka.KafkaSender;
import pe.ask.library.port.out.logger.Logger;
import pe.ask.library.port.out.persistence.IBookRepository;
import pe.ask.library.usecase.utils.UseCase;
import reactor.core.publisher.Mono;

import java.util.UUID;

@UseCase
public class GetBookByIdUseCase implements IGetBookByIdUseCase {

    private final IBookRepository repository;

    public GetBookByIdUseCase(IBookRepository repository) {
        this.repository = repository;
    }

    @Logger
    @Override
    @KafkaSender(topic = "audit-log")
    public Mono<Book> getBookById(UUID id) {
        return repository.getBookById(id)
                .switchIfEmpty(Mono.error(RuntimeException::new));
    }
}
