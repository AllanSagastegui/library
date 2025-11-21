package pe.ask.library.persistence.adapter;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import pe.ask.library.model.category.Category;
import pe.ask.library.persistence.entity.CategoryEntity;
import pe.ask.library.persistence.helper.ReactiveAdapterOperations;
import pe.ask.library.persistence.repository.ICategoryReactiveRepository;
import pe.ask.library.port.out.persistence.ICategoryRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class CategoryReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Category,
        CategoryEntity,
        UUID,
        ICategoryReactiveRepository
        > implements ICategoryRepository {

    public CategoryReactiveRepositoryAdapter(ICategoryReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Category.class));
    }

    @Override
    public Flux<Category> getAllCategories(int page, int size) {
        return super.repository.findAllPaginated(page * size, size)
                .map(this::toEntity);
    }

    @Override
    public Mono<Category> getCategoryById(UUID categoryId) {
        return super.repository.findById(categoryId)
                .map(this::toEntity);
    }

    @Override
    public Flux<Category> getCategoriesByName(int page, int size, String name) {
        return super.repository.findCategoriesByNamePaginated(page * size, size, queryValue(name))
                .map(this::toEntity);
    }

    @Override
    public Mono<Long> countAll() {
        return super.repository.countAll();
    }

    @Override
    public Mono<Long> countAllByName(String name) {
        return super.repository.countAllByName(queryValue(name));
    }

    private String queryValue(String value) {
        return "%" + value + "%";
    }
}
