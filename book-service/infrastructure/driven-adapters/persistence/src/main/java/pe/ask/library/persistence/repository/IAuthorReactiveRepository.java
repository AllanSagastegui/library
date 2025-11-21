package pe.ask.library.persistence.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pe.ask.library.persistence.entity.AuthorEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface IAuthorReactiveRepository extends ReactiveCrudRepository<AuthorEntity, UUID>, ReactiveQueryByExampleExecutor<AuthorEntity> {
    @Query("""
        SELECT * FROM author
        LIMIT :limit 
        OFFSET :offset
       """)
    Flux<AuthorEntity> findAllPaginated(
            @Param("offset") int offset,
            @Param("limit") int limit);

    @Query("""
        SELECT * FROM author
        WHERE nationality ILIKE :nationality
        LIMIT :limit 
        OFFSET :offset
       """)
    Flux<AuthorEntity> findAllAuthorsByNationality(
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("nationality") String nationality);

    Mono<AuthorEntity> findAuthorByPseudonymIsContainingIgnoreCase(String pseudonym);

    @Query("SELECT count(*) FROM author")
    Mono<Long> countAll();

    @Query("""
        SELECT count(*) FROM author 
        WHERE nationality ILIKE :nationality
    """)
    Mono<Long> countAllByNationality(@Param("nationality") String nationality);
}
