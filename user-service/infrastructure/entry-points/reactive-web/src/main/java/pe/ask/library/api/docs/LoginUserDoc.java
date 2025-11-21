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
import pe.ask.library.api.docs.exception.InvalidCredentialsExceptionDoc;
import pe.ask.library.api.docs.exception.UnexpectedExceptionDoc;
import pe.ask.library.api.docs.exception.ValidationExceptionDoc;
import pe.ask.library.api.dto.request.LoginRequest;
import pe.ask.library.api.dto.response.LoginResponse;
import reactor.core.publisher.Mono;

@Component
@Schema(
        name = "UserAuthentication",
        description = "Defines the API contract for user authentication (Login). " +
                        "This endpoint enables users to sign in using their credentials " +
                        "to obtain a JSON Web Token (JWT) for accessing protected API routes."
)
public class LoginUserDoc {

    @ResponseStatus(HttpStatus.OK)
    @Operation(
            operationId = "loginUser",
            summary = "Authenticate a user and obtain a JWT",
            description = "Authenticates a user with their registered email and password. " +
                            "Upon successful authentication, it returns a JWT. " +
                            "This token must be included in the `Authorization` " +
                            "header as a Bearer token for all subsequent requests to secured endpoints."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication successful. Returns a JWT and session details.",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LoginResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request. The request is malformed or contains invalid data (e.g., missing fields).",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ValidationExceptionDoc.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized. Invalid credentials provided (e.g., incorrect email or password).",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = InvalidCredentialsExceptionDoc.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error. An unexpected error occurred on the server side.",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UnexpectedExceptionDoc.class))
            )
    })
    public Mono<LoginResponse> loginDoc(
            @RequestBody(
                    description = "User credentials required for authentication.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginRequest.class)
                    )
            )
            @org.springframework.web.bind.annotation.RequestBody LoginRequest dto) {
        return Mono.empty();
    }
}
