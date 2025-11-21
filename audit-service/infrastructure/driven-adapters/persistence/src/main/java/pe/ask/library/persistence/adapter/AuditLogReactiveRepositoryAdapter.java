package pe.ask.library.persistence.adapter;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import pe.ask.library.model.auditlog.AuditLog;
import pe.ask.library.model.utils.Pageable;
import pe.ask.library.persistence.document.AuditLogDocument;
import pe.ask.library.persistence.helper.AdapterOperations;
import pe.ask.library.persistence.repository.IAuditLogReactiveRepository;
import pe.ask.library.port.out.persistence.IAuditLogRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class AuditLogReactiveRepositoryAdapter extends AdapterOperations<
        AuditLog,
        AuditLogDocument,
        UUID,
        IAuditLogReactiveRepository
        > implements IAuditLogRepository {

    public AuditLogReactiveRepositoryAdapter(IAuditLogReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d-> mapper.map(d, AuditLog.class));
    }


    @Override
    public Mono<AuditLog> saveAuditLog(AuditLog auditLog) {
        return super.repository.save(toData(auditLog))
                .map(this::toEntity);
    }

    @Override
    public Mono<Pageable<AuditLog>> findByUserId(String userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return super.repository.findAllByUserId(userId, pageRequest)
                .map(this::toEntity)
                .collectList()
                .map(list -> Pageable.<AuditLog>builder()
                        .content(list)
                        .page(pageRequest.getPageNumber())
                        .size(pageRequest.getPageSize())
                        .totalElements(list.size())
                        .totalPages(list.size())
                        .build()
                );
    }
}
