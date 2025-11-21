package pe.ask.library.usecase.book;

import pe.ask.library.model.book.BookComplete;
import pe.ask.library.model.utils.Pageable;
import pe.ask.library.port.in.usecase.book.IGetAllBooksByPublisherIdUseCase;
import pe.ask.library.port.in.usecase.utils.IPaginationUtils;
import pe.ask.library.port.out.kafka.KafkaSender;
import pe.ask.library.port.out.logger.Logger;
import pe.ask.library.port.out.persistence.IBookRepository;
import pe.ask.library.usecase.utils.MapBookToComplete;
import pe.ask.library.usecase.utils.UseCase;
import reactor.core.publisher.Mono;

import java.util.UUID;

@UseCase
public class GetAllBooksByPublisherIdUseCase implements IGetAllBooksByPublisherIdUseCase {

    private final IBookRepository repository;
    private final MapBookToComplete mapBookToComplete;
    private final IPaginationUtils utils;

    public GetAllBooksByPublisherIdUseCase(
            IBookRepository repository,
            MapBookToComplete mapBookToComplete,
            IPaginationUtils utils
    ) {
        this.repository = repository;
        this.mapBookToComplete = mapBookToComplete;
        this.utils = utils;
    }

    @Logger
    @Override
    @KafkaSender(topic = "audit-log")
    public Mono<Pageable<BookComplete>> getAllBooksByPublisherId(int page, int size, UUID publisherId) {
        return utils.createPageable(
                repository.getAllBooksByPublisherId(page, size, publisherId)
                        .flatMap(mapBookToComplete::map),
                repository.countAllByPublisherId(publisherId),
                page,
                size
        );
    }
}
