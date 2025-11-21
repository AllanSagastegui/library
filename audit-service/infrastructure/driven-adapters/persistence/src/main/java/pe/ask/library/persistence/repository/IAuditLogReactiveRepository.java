package pe.ask.library.persistence.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import pe.ask.library.persistence.document.AuditLogDocument;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface IAuditLogReactiveRepository extends ReactiveElasticsearchRepository<AuditLogDocument, UUID>, ReactiveQueryByExampleExecutor<AuditLogDocument> {
    Flux<AuditLogDocument> findAllByUserId(String userId, Pageable pageable);
}
