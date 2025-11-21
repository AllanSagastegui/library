package pe.ask.library.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body for user registration")
public record RegisterRequest(
        @Schema(description = "User's name", example = "John", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Name cannot be empty")
        String name,
        @Schema(description = "User's last name", example = "Doe", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Last name cannot be empty")
        String lastName,
        @Schema(description = "User's email address", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Email cannot be empty")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Invalid email pattern")
        String email,
        @Schema(description = "User's password", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Password cannot be empty")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password
) {
}