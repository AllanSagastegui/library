package pe.ask.library.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Request body for user login")
public record LoginRequest(
        @Schema(description = "User's email address", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Email cannot be empty")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Invalid email pattern")
        String email,
        @Schema(description = "User's password", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Password cannot be empty")
        String password
) {
}