package pe.ask.library.port.out.persistence;

import pe.ask.library.model.category.Category;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ICategoryRepository {
    Flux<Category> getAllCategories(int page, int size);
    Mono<Category> getCategoryById(UUID categoryId);
    Flux<Category> getCategoriesByName(int page, int size, String name);
    Mono<Long> countAll();
    Mono<Long> countAllByName(String name);
}
