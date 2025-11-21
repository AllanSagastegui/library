package pe.ask.library.port.in.usecase.category;

import pe.ask.library.model.category.Category;
import reactor.core.publisher.Mono;

import java.util.UUID;

@FunctionalInterface
public interface IGetCategoryByIdUseCase {
    Mono<Category> getCategoryById(UUID categoryId);
}
