package pe.ask.library.model.auditlog;

import java.time.LocalDateTime;
import java.util.UUID;

public class AuditLogBuilder {

    private UUID id;
    private String msName;
    private String methodName;
    private String eventName;
    private String userId;
    private LocalDateTime timestamp;

    public AuditLogBuilder() { }

    public AuditLogBuilder id(UUID id) {
        this.id = id;
        return this;
    }

    public AuditLogBuilder msName(String msName) {
        this.msName = msName;
        return this;
    }

    public AuditLogBuilder methodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public AuditLogBuilder eventName(String eventName) {
        this.eventName = eventName;
        return this;
    }

    public AuditLogBuilder userId(String userId) {
        this.userId = userId;
        return this;
    }

    public AuditLogBuilder timestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public AuditLog build() {
        return new AuditLog(
                id,
                msName,
                methodName,
                eventName,
                userId,
                timestamp
        );
    }
}