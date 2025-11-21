package pe.ask.library.usecase.category;

import pe.ask.library.model.category.Category;
import pe.ask.library.port.in.usecase.category.IGetCategoryByIdUseCase;
import pe.ask.library.port.out.kafka.KafkaSender;
import pe.ask.library.port.out.logger.Logger;
import pe.ask.library.port.out.persistence.ICategoryRepository;
import pe.ask.library.usecase.utils.UseCase;
import reactor.core.publisher.Mono;

import java.util.UUID;

@UseCase
public class GetCategoryByIdUseCase implements IGetCategoryByIdUseCase {

    private final ICategoryRepository repository;

    public GetCategoryByIdUseCase(ICategoryRepository repository) {
        this.repository = repository;
    }

    @Logger
    @Override
    @KafkaSender(topic = "audit-log")
    public Mono<Category> getCategoryById(UUID categoryId) {
        return repository.getCategoryById(categoryId)
                .switchIfEmpty(Mono.error(RuntimeException::new));
    }
}
