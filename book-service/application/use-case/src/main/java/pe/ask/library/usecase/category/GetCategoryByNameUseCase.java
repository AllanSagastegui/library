package pe.ask.library.usecase.category;

import pe.ask.library.model.category.Category;
import pe.ask.library.model.utils.Pageable;
import pe.ask.library.port.in.usecase.category.IGetCategoryByNameUseCase;
import pe.ask.library.port.in.usecase.utils.IPaginationUtils;
import pe.ask.library.port.out.kafka.KafkaSender;
import pe.ask.library.port.out.logger.Logger;
import pe.ask.library.port.out.persistence.ICategoryRepository;
import pe.ask.library.usecase.utils.UseCase;
import reactor.core.publisher.Mono;

@UseCase
public class GetCategoryByNameUseCase implements IGetCategoryByNameUseCase {

    private final ICategoryRepository repository;
    private final IPaginationUtils utils;

    public GetCategoryByNameUseCase(ICategoryRepository repository, IPaginationUtils utils) {
        this.repository = repository;
        this.utils = utils;
    }

    @Logger
    @Override
    @KafkaSender(topic = "audit-log")
    public Mono<Pageable<Category>> getCategoryByName(int page, int size, String name) {
        return utils.createPageable(
                repository.getCategoriesByName(page, size, name),
                repository.countAllByName(name),
                page,
                size
        );
    }
}
