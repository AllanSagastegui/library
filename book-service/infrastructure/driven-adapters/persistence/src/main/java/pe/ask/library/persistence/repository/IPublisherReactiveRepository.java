package pe.ask.library.persistence.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pe.ask.library.persistence.entity.PublisherEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface IPublisherReactiveRepository extends ReactiveCrudRepository<PublisherEntity, UUID>, ReactiveQueryByExampleExecutor<PublisherEntity> {
    @Query("""
        SELECT * FROM publisher
        LIMIT :limit 
        OFFSET :offset
       """)
    Flux<PublisherEntity> findAllPaginated(
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    @Query("""
        SELECT * FROM publisher
        WHERE name ILIKE :name
        LIMIT :limit 
        OFFSET :offset
    """)
    Flux<PublisherEntity> findPublisherByNamePaginated(
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("name") String name);

    @Query("""
        SELECT * FROM publisher
        WHERE country ILIKE :country
        LIMIT :limit 
        OFFSET :offset
    """)
    Flux<PublisherEntity> findPublisherByCountryPaginated(
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("country") String country);

    @Query("SELECT count(*) FROM publisher")
    Mono<Long> countAll();

    @Query("""
        SELECT count(*) FROM publisher
        WHERE name ILIKE :name
    """)
    Mono<Long> countAllByName(
            @Param("name") String name
    );

    @Query("""
        SELECT count(*) FROM publisher
        WHERE country ILIKE CONCAT('%', :country, '%')
    """)
    Mono<Long> countAllByCountry(
            @Param("country") String country
    );
}
