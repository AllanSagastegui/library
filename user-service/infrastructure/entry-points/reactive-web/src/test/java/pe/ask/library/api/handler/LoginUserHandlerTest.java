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
import pe.ask.library.api.dto.request.LoginRequest;
import pe.ask.library.api.dto.response.LoginResponse;
import pe.ask.library.api.exception.UnexpectedException;
import pe.ask.library.api.utils.validation.CustomValidator;
import pe.ask.library.model.token.Token;
import pe.ask.library.port.in.usecase.user.ILoginUserUseCase;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUserHandlerTest {

    @Mock
    private ILoginUserUseCase useCase;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private CustomValidator validator;

    @Mock
    private ServerRequest serverRequest;

    private LoginUserHandler handler;

    @BeforeEach
    void setUp() {
        handler = new LoginUserHandler(useCase, mapper, validator);
    }

    @Test
    @DisplayName("Should return 200 OK and LoginResponse when credentials are valid")
    void listenPOSTLoginUserUseCaseSuccess() {
        String email = "test@email.com";
        String password = "password";

        LoginRequest requestDto = new LoginRequest();
        requestDto.setEmail(email);
        requestDto.setPassword(password);

        Token domainToken = Token.builder().token("jwt-token-xyz").build();
        LoginResponse responseDto = new LoginResponse().builder().token("jwt-token-xyz").build();

        when(serverRequest.bodyToMono(LoginRequest.class)).thenReturn(Mono.just(requestDto));
        when(validator.validate(requestDto)).thenReturn(Mono.just(requestDto));

        when(useCase.loginUser(email, password)).thenReturn(Mono.just(domainToken));

        when(mapper.map(domainToken, LoginResponse.class)).thenReturn(responseDto);

        StepVerifier.create(handler.listenPOSTLoginUserUseCase(serverRequest))
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Should propagate error when UseCase fails (e.g. Invalid Credentials)")
    void listenPOSTLoginUserUseCaseError() {
        LoginRequest requestDto = new LoginRequest();
        requestDto.setEmail("fail@email.com");
        requestDto.setPassword("wrong");

        when(serverRequest.bodyToMono(LoginRequest.class)).thenReturn(Mono.just(requestDto));
        when(validator.validate(requestDto)).thenReturn(Mono.just(requestDto));

        when(useCase.loginUser(any(), any())).thenReturn(Mono.error(new RuntimeException("Invalid credentials")));

        StepVerifier.create(handler.listenPOSTLoginUserUseCase(serverRequest))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    @DisplayName("Should fail when request body validation fails")
    void listenPOSTLoginUserUseCaseValidationError() {
        LoginRequest invalidDto = new LoginRequest();

        when(serverRequest.bodyToMono(LoginRequest.class)).thenReturn(Mono.just(invalidDto));
        when(validator.validate(invalidDto)).thenReturn(Mono.error(new RuntimeException("Validation error")));

        StepVerifier.create(handler.listenPOSTLoginUserUseCase(serverRequest))
                .expectErrorSatisfies(throwable -> {
                    assertThat(throwable).isInstanceOf(UnexpectedException.class);
                    assertThat(throwable.getMessage()).contains("Something went wrong");
                    assertThat(throwable.getCause()).hasMessage("Validation error");
                })
                .verify();
    }
}