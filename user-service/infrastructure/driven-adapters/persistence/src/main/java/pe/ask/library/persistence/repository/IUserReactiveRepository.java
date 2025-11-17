package pe.ask.library.persistence.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import pe.ask.library.persistence.document.UserDocument;
import reactor.core.publisher.Mono;

public interface IUserReactiveRepository extends ReactiveMongoRepository<UserDocument, String>, ReactiveQueryByExampleExecutor<UserDocument> {
    Mono<Boolean> existsByEmail(String email);

    Mono<UserDocument> findByEmail(String email);
}
