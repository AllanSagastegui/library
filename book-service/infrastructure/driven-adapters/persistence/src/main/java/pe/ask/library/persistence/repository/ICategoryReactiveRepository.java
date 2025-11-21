package pe.ask.library.persistence.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pe.ask.library.persistence.entity.CategoryEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ICategoryReactiveRepository extends ReactiveCrudRepository<CategoryEntity, UUID>, ReactiveQueryByExampleExecutor<CategoryEntity> {
    @Query("""
        SELECT * FROM category
        LIMIT :limit 
        OFFSET :offset
       """)
    Flux<CategoryEntity> findAllPaginated(
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    @Query("""
        SELECT * FROM category
        WHERE name ILIKE :name
        LIMIT :limit 
        OFFSET :offset
    """)
    Flux<CategoryEntity> findCategoriesByNamePaginated(
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("name") String name
    );

    @Query("SELECT count(*) FROM category")
    Mono<Long> countAll();

    @Query("""
        SELECT count(*) FROM category
        WHERE name ILIKE :name
    """)
    Mono<Long> countAllByName(
            @Param("name") String name
    );
}
