package pe.ask.library.api.dto.response;

import java.time.LocalDateTime;

public record RegisterResponse(
        String name,
        String lastName,
        String email,
        String password,
        LocalDateTime createdAt
) {
}
