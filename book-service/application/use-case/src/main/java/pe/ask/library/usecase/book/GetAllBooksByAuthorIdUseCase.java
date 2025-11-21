package pe.ask.library.usecase.book;

import pe.ask.library.model.book.BookComplete;
import pe.ask.library.model.utils.Pageable;
import pe.ask.library.port.in.usecase.book.IGetAllBooksByAuthorIdUseCase;
import pe.ask.library.port.in.usecase.utils.IPaginationUtils;
import pe.ask.library.port.out.kafka.KafkaSender;
import pe.ask.library.port.out.logger.Logger;
import pe.ask.library.port.out.persistence.IBookRepository;
import pe.ask.library.usecase.utils.MapBookToComplete;
import pe.ask.library.usecase.utils.PaginationUtils;
import pe.ask.library.usecase.utils.UseCase;
import reactor.core.publisher.Mono;

import java.util.UUID;

@UseCase
public class GetAllBooksByAuthorIdUseCase implements IGetAllBooksByAuthorIdUseCase {

    private final IBookRepository repository;
    private final MapBookToComplete mapBookToComplete;
    private final IPaginationUtils utils;

    public GetAllBooksByAuthorIdUseCase(
            IBookRepository repository,
            MapBookToComplete mapBookToComplete,
            PaginationUtils utils
    ) {
        this.repository = repository;
        this.mapBookToComplete = mapBookToComplete;
        this.utils = utils;
    }

    @Logger
    @Override
    @KafkaSender(topic = "audit-log")
    public Mono<Pageable<BookComplete>> getAllBooksByAuthorId(int page, int size, UUID authorId) {
        return utils.createPageable(
                repository.getAllBooksByAuthorId(page, size, authorId)
                        .flatMap(mapBookToComplete::map),
                repository.countAllByAuthorId(authorId),
                page,
                size
        );
    }
}
