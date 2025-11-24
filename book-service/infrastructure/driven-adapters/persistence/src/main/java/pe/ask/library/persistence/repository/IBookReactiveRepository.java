package pe.ask.library.persistence.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pe.ask.library.persistence.entity.BookEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface IBookReactiveRepository extends ReactiveCrudRepository<BookEntity, UUID>, ReactiveQueryByExampleExecutor<BookEntity> {

    @Query("SELECT * FROM book WHERE gender ILIKE :gender OFFSET :offset LIMIT :limit")
    Flux<BookEntity> findByGender(@Param("gender") String gender, @Param("offset") int offset, @Param("limit") int limit);

    @Query("SELECT count(*) FROM book WHERE gender ILIKE :gender")
    Mono<Long> countByGender(@Param("gender") String gender);

    @Query("SELECT * FROM book WHERE title ILIKE :title OFFSET :offset LIMIT :limit")
    Flux<BookEntity> findByTitle(@Param("title") String title, @Param("offset") int offset, @Param("limit") int limit);

    @Query("SELECT count(*) FROM book WHERE title ILIKE :title")
    Mono<Long> countByTitle(@Param("title") String title);

    @Query("SELECT * FROM book WHERE language ILIKE :language OFFSET :offset LIMIT :limit")
    Flux<BookEntity> findByLanguage(@Param("language") String language, @Param("offset") int offset, @Param("limit") int limit);

    @Query("SELECT count(*) FROM book WHERE language ILIKE :language")
    Mono<Long> countByLanguage(@Param("language") String language);

    @Query("SELECT * FROM book WHERE format = :format OFFSET :offset LIMIT :limit")
    Flux<BookEntity> findByFormat(@Param("format") String format, @Param("offset") int offset, @Param("limit") int limit);

    @Query("SELECT count(*) FROM book WHERE format = :format")
    Mono<Long> countByFormat(@Param("format") String format);

    @Query("SELECT * FROM book WHERE publisher_id = :publisherId OFFSET :offset LIMIT :limit")
    Flux<BookEntity> findByPublisherId(@Param("publisherId") UUID publisherId, @Param("offset") int offset, @Param("limit") int limit);

    @Query("SELECT count(*) FROM book WHERE publisher_id = :publisherId")
    Mono<Long> countByPublisherId(@Param("publisherId") UUID publisherId);

    @Query("SELECT * FROM book WHERE author_id = :authorId OFFSET :offset LIMIT :limit")
    Flux<BookEntity> findByAuthorId(@Param("authorId") UUID authorId, @Param("offset") int offset, @Param("limit") int limit);

    @Query("SELECT count(*) FROM book WHERE author_id = :authorId")
    Mono<Long> countByAuthorId(@Param("authorId") UUID authorId);

    @Query("SELECT * FROM book WHERE category_id = :categoryId OFFSET :offset LIMIT :limit")
    Flux<BookEntity> findByCategoryId(@Param("categoryId") UUID categoryId, @Param("offset") int offset, @Param("limit") int limit);

    @Query("SELECT count(*) FROM book WHERE category_id = :categoryId")
    Mono<Long> countByCategoryId(@Param("categoryId") UUID categoryId);

    @Query("SELECT * FROM book OFFSET :offset LIMIT :limit")
    Flux<BookEntity> findAllPaginated(@Param("offset") int offset, @Param("limit") int limit);

    @Query("SELECT count(*) FROM book")
    Mono<Long> countAll();

    @Query("SELECT EXISTS(SELECT 1 FROM book WHERE id = :id AND stock > 0)")
    Mono<Boolean> validateBookStock(@Param("id") UUID id);
}