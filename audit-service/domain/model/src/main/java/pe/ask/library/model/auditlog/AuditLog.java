package pe.ask.library.model.auditlog;

import java.time.LocalDateTime;
import java.util.UUID;

public class AuditLog {
    private UUID id;
    private String msName;
    private String methodName;
    private String eventName;
    private String userId;
    private LocalDateTime timestamp;

    public AuditLog() {}

    public AuditLog(UUID id, String msName, String methodName, String eventName, String userId, LocalDateTime timestamp) {
        this.id = id;
        this.msName = msName;
        this.methodName = methodName;
        this.eventName = eventName;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public static AuditLogBuilder builder() {
        return new AuditLogBuilder();
    }

    public AuditLogBuilder toBuilder() {
        return new AuditLogBuilder()
                .id(this.id)
                .msName(this.msName)
                .methodName(this.methodName)
                .eventName(this.eventName)
                .userId(this.userId)
                .timestamp(this.timestamp);
    }

    // --- Getters y Setters ---

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMsName() {
        return msName;
    }

    public void setMsName(String msName) {
        this.msName = msName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}