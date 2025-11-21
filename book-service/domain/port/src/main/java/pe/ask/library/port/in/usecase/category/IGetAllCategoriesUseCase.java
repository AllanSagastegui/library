package pe.ask.library.port.in.usecase.category;

import pe.ask.library.model.category.Category;
import pe.ask.library.model.utils.Pageable;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface IGetAllCategoriesUseCase {
    Mono<Pageable<Category>> getAllCategories(int page, int size);
}
