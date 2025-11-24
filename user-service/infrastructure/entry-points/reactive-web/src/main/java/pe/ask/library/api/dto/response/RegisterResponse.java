package pe.ask.library.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response body for user registration")
public class RegisterResponse {

    @Schema(description = "User's name", example = "John")
    private String name;

    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @Schema(description = "User's email address", example = "user@example.com")
    private String email;

    @Schema(description = "User creation date and time")
    private LocalDateTime createdAt;
}