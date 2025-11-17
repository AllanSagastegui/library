package pe.ask.library.persistence.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import pe.ask.library.persistence.document.RoleDocument;
import reactor.core.publisher.Mono;

public interface IRoleReactiveRepository extends ReactiveMongoRepository<RoleDocument, String>, ReactiveQueryByExampleExecutor<RoleDocument> {
    Mono<RoleDocument> findByName(String name);
}
