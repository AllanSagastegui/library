package pe.ask.library.port.in.usecase.auditlog;

import pe.ask.library.model.auditlog.AuditLog;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ICreateNewAuditLogUseCase {
    Mono<Void> execute(AuditLog auditLog);
}
