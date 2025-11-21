package pe.ask.library.port.in.usecase.auditlog;

import pe.ask.library.model.auditlog.AuditLog;
import pe.ask.library.model.utils.Pageable;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface IGetAllAuditLogByUserIdUseCase {
    Mono<Pageable<AuditLog>> execute(String userId, int page, int size);
}
