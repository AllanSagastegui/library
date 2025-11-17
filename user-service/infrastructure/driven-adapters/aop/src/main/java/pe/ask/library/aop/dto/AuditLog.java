package pe.ask.library.aop.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AuditLog(
        UUID id,
        String msName,
        String methodName,
        String eventName,
        String userId,
        LocalDateTime timestamp
) { }