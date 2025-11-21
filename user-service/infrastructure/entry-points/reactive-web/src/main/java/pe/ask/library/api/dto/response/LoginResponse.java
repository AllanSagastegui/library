package pe.ask.library.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Response body for user login")
public record LoginResponse(
        @Schema(description = "JWT token for authentication", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaWF0IjoxNjE2MjM5MDIyLCJleHAiOjE2MTYyNDI2MjJ9")
        String token,
        @Schema(description = "Token type", example = "Bearer")
        String type,
        @Schema(description = "Token expiration date and time")
        LocalDateTime expiresAt,
        @Schema(description = "Token creation date and time")
        LocalDateTime createdAt
) {
}
