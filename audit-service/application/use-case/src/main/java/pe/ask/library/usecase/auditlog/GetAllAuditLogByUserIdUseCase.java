package pe.ask.library.usecase.auditlog;

import pe.ask.library.model.auditlog.AuditLog;
import pe.ask.library.model.utils.Pageable;
import pe.ask.library.port.in.usecase.auditlog.IGetAllAuditLogByUserIdUseCase;
import pe.ask.library.port.out.logger.Logger;
import pe.ask.library.port.out.persistence.IAuditLogRepository;
import pe.ask.library.usecase.utils.UseCase;
import reactor.core.publisher.Mono;

@UseCase
public class GetAllAuditLogByUserIdUseCase implements IGetAllAuditLogByUserIdUseCase {

    private final IAuditLogRepository repository;

    public GetAllAuditLogByUserIdUseCase(IAuditLogRepository repository) {
        this.repository = repository;
    }

    @Logger
    @Override
    public Mono<Pageable<AuditLog>> execute(String userId, int page, int size) {
        return repository.findByUserId(userId, page, size);
    }
}
