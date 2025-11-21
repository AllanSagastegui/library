package pe.ask.library.port.in.usecase.category;

import pe.ask.library.model.category.Category;
import pe.ask.library.model.utils.Pageable;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface IGetCategoryByNameUseCase {
    Mono<Pageable<Category>> getCategoryByName(int page, int size, String name);
}
