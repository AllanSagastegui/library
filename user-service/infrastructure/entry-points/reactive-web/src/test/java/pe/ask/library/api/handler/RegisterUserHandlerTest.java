package pe.ask.library.api.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.util.UriBuilder;
import pe.ask.library.api.dto.request.RegisterRequest;
import pe.ask.library.api.dto.response.RegisterResponse;
import pe.ask.library.api.utils.validation.CustomValidator;
import pe.ask.library.model.user.User;
import pe.ask.library.port.in.usecase.user.IRegisterUserUseCase;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterUserHandlerTest {

    @Mock
    private IRegisterUserUseCase useCase;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private CustomValidator validator;

    @Mock
    private ServerRequest serverRequest;

    @Mock
    private UriBuilder uriBuilder;

    private RegisterUserHandler handler;

    @BeforeEach
    void setUp() {
        handler = new RegisterUserHandler(useCase, mapper, validator);
    }

    @Test
    @DisplayName("Should register user and return 201 Created with location header")
    void listenPOSTRegisterUserUseCaseSuccess() {
        RegisterRequest requestDto = new RegisterRequest();
        User userDomain = new User();
        User savedUser = User.builder().id("user-123").build();
        RegisterResponse responseDto = new RegisterResponse();
        URI createdUri = URI.create("/user-123");

        when(serverRequest.bodyToMono(RegisterRequest.class)).thenReturn(Mono.just(requestDto));
        when(validator.validate(requestDto)).thenReturn(Mono.just(requestDto));
        when(mapper.map(requestDto, User.class)).thenReturn(userDomain);
        when(useCase.registerUser(userDomain)).thenReturn(Mono.just(savedUser));
        when(mapper.map(savedUser, RegisterResponse.class)).thenReturn(responseDto);

        when(serverRequest.uriBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.path(anyString())).thenReturn(uriBuilder);

        when(uriBuilder.build(any(Object[].class))).thenReturn(createdUri);

        StepVerifier.create(handler.listenPOSTRegisterUserUseCase(serverRequest))
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED);
                    assertThat(response.headers().getLocation()).isEqualTo(createdUri);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Should fail when request body validation fails")
    void listenPOSTRegisterUserUseCaseValidationError() {
        RegisterRequest requestDto = new RegisterRequest();

        when(serverRequest.bodyToMono(RegisterRequest.class)).thenReturn(Mono.just(requestDto));
        when(validator.validate(requestDto)).thenReturn(Mono.error(new RuntimeException("Validation error")));

        StepVerifier.create(handler.listenPOSTRegisterUserUseCase(serverRequest))
                .expectError()
                .verify();
    }

    @Test
    @DisplayName("Should propagate error when use case fails (e.g. User exists)")
    void listenPOSTRegisterUserUseCaseBusinessError() {
        RegisterRequest requestDto = new RegisterRequest();
        User userDomain = new User();

        when(serverRequest.bodyToMono(RegisterRequest.class)).thenReturn(Mono.just(requestDto));
        when(validator.validate(requestDto)).thenReturn(Mono.just(requestDto));
        when(mapper.map(requestDto, User.class)).thenReturn(userDomain);
        when(useCase.registerUser(userDomain)).thenReturn(Mono.error(new RuntimeException("User exists")));

        StepVerifier.create(handler.listenPOSTRegisterUserUseCase(serverRequest))
                .expectError(RuntimeException.class)
                .verify();
    }
}