package pe.ask.library.persistence.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import pe.ask.library.model.category.Category;
import pe.ask.library.persistence.entity.CategoryEntity;
import pe.ask.library.persistence.repository.ICategoryReactiveRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryReactiveRepositoryAdapterTest {

    @Mock
    private ICategoryReactiveRepository repository;

    @Mock
    private ObjectMapper mapper;

    private CategoryReactiveRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new CategoryReactiveRepositoryAdapter(repository, mapper);
    }

    @Test
    @DisplayName("Should get all categories paginated")
    void getAllCategories() {
        CategoryEntity entity = new CategoryEntity();
        Category domain = new Category();

        when(repository.findAllPaginated(0, 10)).thenReturn(Flux.just(entity));
        when(mapper.map(entity, Category.class)).thenReturn(domain);

        StepVerifier.create(adapter.getAllCategories(0, 10))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should get category by ID")
    void getCategoryById() {
        UUID id = UUID.randomUUID();
        CategoryEntity entity = new CategoryEntity();
        Category domain = new Category();

        when(repository.findById(id)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Category.class)).thenReturn(domain);

        StepVerifier.create(adapter.getCategoryById(id))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should get categories by name with formatting")
    void getCategoriesByName() {
        CategoryEntity entity = new CategoryEntity();
        Category domain = new Category();

        when(repository.findCategoriesByNamePaginated(0, 10, "%Horror%")).thenReturn(Flux.just(entity));
        when(mapper.map(entity, Category.class)).thenReturn(domain);

        StepVerifier.create(adapter.getCategoriesByName(0, 10, "Horror"))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should count all categories")
    void countAll() {
        when(repository.countAll()).thenReturn(Mono.just(5L));
        StepVerifier.create(adapter.countAll())
                .expectNext(5L)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should count all categories by name")
    void countAllByName() {
        when(repository.countAllByName("%Horror%")).thenReturn(Mono.just(2L));
        StepVerifier.create(adapter.countAllByName("Horror"))
                .expectNext(2L)
                .verifyComplete();
    }
}