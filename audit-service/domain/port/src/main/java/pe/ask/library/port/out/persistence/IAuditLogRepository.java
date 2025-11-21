package pe.ask.library.port.out.persistence;

import pe.ask.library.model.auditlog.AuditLog;
import pe.ask.library.model.utils.Pageable;
import reactor.core.publisher.Mono;

public interface IAuditLogRepository {
    Mono<AuditLog> saveAuditLog(AuditLog auditLog);
    Mono<Pageable<AuditLog>> findByUserId(String userId, int page, int size);
}
