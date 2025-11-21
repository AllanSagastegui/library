package pe.ask.library.usecase.book;

import pe.ask.library.model.book.BookComplete;
import pe.ask.library.model.utils.Pageable;
import pe.ask.library.port.in.usecase.book.IGetAllBooksByGenderUseCase;
import pe.ask.library.port.in.usecase.utils.IPaginationUtils;
import pe.ask.library.port.out.kafka.KafkaSender;
import pe.ask.library.port.out.logger.Logger;
import pe.ask.library.port.out.persistence.IBookRepository;
import pe.ask.library.usecase.utils.MapBookToComplete;
import pe.ask.library.usecase.utils.UseCase;
import reactor.core.publisher.Mono;

@UseCase
public class GetAllBooksByGenderUseCase implements IGetAllBooksByGenderUseCase {

    private final IBookRepository repository;
    private final MapBookToComplete mapBookToComplete;
    private final IPaginationUtils utils;

    public GetAllBooksByGenderUseCase(
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
    public Mono<Pageable<BookComplete>> getAllBooksByGender(int page, int size, String gender) {
        return utils.createPageable(
                repository.getAllBooksByGender(page, size, gender)
                        .flatMap(mapBookToComplete::map),
                repository.countAllByGender(gender),
                page,
                size
        );
    }
}
