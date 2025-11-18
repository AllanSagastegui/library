package pe.ask.library.api.dto.response;

import java.time.LocalDateTime;

public record LoginResponse(
        String token,
        String type,
        LocalDateTime expiresAt,
        LocalDateTime createdAt
) {
}
