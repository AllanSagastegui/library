package pe.ask.library.usecase.category;

import pe.ask.library.model.category.Category;
import pe.ask.library.model.utils.Pageable;
import pe.ask.library.port.in.usecase.category.IGetAllCategoriesUseCase;
import pe.ask.library.port.in.usecase.utils.IPaginationUtils;
import pe.ask.library.port.out.kafka.KafkaSender;
import pe.ask.library.port.out.logger.Logger;
import pe.ask.library.port.out.persistence.ICategoryRepository;
import pe.ask.library.usecase.utils.UseCase;
import reactor.core.publisher.Mono;

@UseCase
public class GetAllCategoriesUseCase implements IGetAllCategoriesUseCase {

    private final ICategoryRepository repository;
    private final IPaginationUtils utils;

    public GetAllCategoriesUseCase(ICategoryRepository repository, IPaginationUtils utils) {
        this.repository = repository;
        this.utils = utils;
    }

    @Logger
    @Override
    @KafkaSender(topic = "audit-log")
    public Mono<Pageable<Category>> getAllCategories(int page, int size) {
        return utils.createPageable(
                repository.getAllCategories(page, size),
                repository.countAll(),
                page,
                size
        );
    }
}
