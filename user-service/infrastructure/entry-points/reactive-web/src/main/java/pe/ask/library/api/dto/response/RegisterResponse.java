package pe.ask.library.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Response body for user registration")
public record RegisterResponse(
        @Schema(description = "User's name", example = "John")
        String name,
        @Schema(description = "User's last name", example = "Doe")
        String lastName,
        @Schema(description = "User's email address", example = "user@example.com")
        String email,
        @Schema(description = "User creation date and time")
        LocalDateTime createdAt
) {
}