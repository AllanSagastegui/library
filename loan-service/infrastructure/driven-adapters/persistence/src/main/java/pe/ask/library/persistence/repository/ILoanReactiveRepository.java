package pe.ask.library.persistence.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pe.ask.library.persistence.entity.LoanEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ILoanReactiveRepository extends ReactiveCrudRepository<LoanEntity, UUID>, ReactiveQueryByExampleExecutor<LoanEntity> {


    @Query("SELECT * FROM loan OFFSET :offset LIMIT :limit")
    Flux<LoanEntity> findAllPaginated(@Param("offset") int offset, @Param("limit") int limit);


    @Query("SELECT * FROM loan WHERE user_id = :userId OFFSET :offset LIMIT :limit")
    Flux<LoanEntity> getAllLoansByUserId(@Param("userId") UUID userId, @Param("offset") int offset, @Param("limit") int limit);

    @Query("SELECT * FROM loan WHERE book_id = :bookId OFFSET :offset LIMIT :limit")
    Flux<LoanEntity> getAllLoansByBookId(@Param("bookId") UUID userId, @Param("offset") int offset, @Param("limit") int limit);

    @Query("SELECT count(*) FROM loan")
    Mono<Long> countAll();

    @Query("SELECT count(*) FROM loan WHERE user_id = :userId")
    Mono<Long> countAllByUserId(@Param("userId") UUID userId);

    @Query("SELECT count(*) FROM loan WHERE book_id = :bookId")
    Mono<Long> countAllByBookId(@Param("bookId") UUID bookId);

}
