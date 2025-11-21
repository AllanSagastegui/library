package pe.ask.library.usecase.auditlog;

import pe.ask.library.model.auditlog.AuditLog;
import pe.ask.library.port.in.usecase.auditlog.ICreateNewAuditLogUseCase;
import pe.ask.library.port.out.logger.Logger;
import pe.ask.library.port.out.persistence.IAuditLogRepository;
import pe.ask.library.usecase.utils.UseCase;
import reactor.core.publisher.Mono;

@UseCase
public class CreateNewAuditLogUseCase implements ICreateNewAuditLogUseCase {

    private final IAuditLogRepository repository;

    public CreateNewAuditLogUseCase(IAuditLogRepository repository) {
        this.repository = repository;
    }

    @Logger
    @Override
    public Mono<Void> execute(AuditLog auditLog) {
        return repository.saveAuditLog(auditLog)
                .then();
    }
}
