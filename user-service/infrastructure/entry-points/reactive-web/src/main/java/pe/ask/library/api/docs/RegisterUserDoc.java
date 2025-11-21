package pe.ask.library.api.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import pe.ask.library.api.docs.exception.UnexpectedExceptionDoc;
import pe.ask.library.api.docs.exception.UserAlreadyExistsExceptionDoc;
import pe.ask.library.api.docs.exception.ValidationExceptionDoc;
import pe.ask.library.api.dto.request.RegisterRequest;
import pe.ask.library.api.dto.response.RegisterResponse;
import reactor.core.publisher.Mono;

@Component
@Schema(
        name = "UserRegistration",
        description = "Defines the API contract for new user registration (Sign-Up). " +
                        "This endpoint allows a new user to create an account by providing " +
                        "their personal details, including name, email, and password."
)
public class RegisterUserDoc {
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            operationId = "registerUser",
            summary = "Register a new user",
            description = "Creates a new user account based on the provided registration data. " +
                            "If registration is successful, it returns the newly created user's information. " +
                            "The email provided must be unique."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User registered successfully. Returns the created user's data.",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RegisterResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request. The request is malformed or contains invalid data (e.g., invalid email format or missing fields).",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ValidationExceptionDoc.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict. A user with the specified email address already exists.",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserAlreadyExistsExceptionDoc.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error. An unexpected error occurred on the server side.",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UnexpectedExceptionDoc.class))
            )
    })
    public Mono<RegisterResponse> registerDoc(
            @RequestBody(
                    description = "User registration data required to create a new account.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegisterRequest.class)
                    )
            )
            @org.springframework.web.bind.annotation.RequestBody RegisterRequest dto) {
        return Mono.empty();
    }
}
