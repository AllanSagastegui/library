package pe.ask.library.usecase.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.ask.library.model.token.Token;
import pe.ask.library.model.user.User;
import pe.ask.library.port.out.persistence.IUserRepository;
import pe.ask.library.port.out.security.IPasswordEncoder;
import pe.ask.library.port.out.security.ITokenProvider;
import pe.ask.library.usecase.utils.exception.InvalidCredentialsException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUserUseCaseTest {

    @Mock
    private IUserRepository userRepository;
    @Mock
    private IPasswordEncoder passwordEncoder;
    @Mock
    private ITokenProvider tokenProvider;

    private LoginUserUseCase loginUserUseCase;

    @BeforeEach
    void setUp() {
        loginUserUseCase = new LoginUserUseCase(
                userRepository,
                passwordEncoder,
                tokenProvider
        );
    }

    @Test
    @DisplayName("Should login user successfully and return a token")
    void loginUserSuccess() {
        String email = "test@email.com";
        String password = "password";
        User user = User.builder().email(email).password("encodedPassword").build();
        Token token = Token.builder().token("test-token").build();

        when(userRepository.getByEmail(email)).thenReturn(Mono.just(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(Mono.just(true));
        when(tokenProvider.generateToken(user)).thenReturn(Mono.just(token));

        StepVerifier.create(loginUserUseCase.loginUser(email, password))
                .expectNext(token)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return InvalidCredentialsException for non-existent email")
    void loginUserInvalidEmail() {
        String email = "nonexistent@email.com";
        String password = "password";

        when(userRepository.getByEmail(email)).thenReturn(Mono.empty());

        StepVerifier.create(loginUserUseCase.loginUser(email, password))
                .expectError(InvalidCredentialsException.class)
                .verify();
    }

    @Test
    @DisplayName("Should return InvalidCredentialsException for incorrect password")
    void loginUserInvalidPassword() {
        String email = "test@email.com";
        String password = "wrongpassword";
        User user = User.builder().email(email).password("encodedPassword").build();

        when(userRepository.getByEmail(email)).thenReturn(Mono.just(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(Mono.just(false));

        StepVerifier.create(loginUserUseCase.loginUser(email, password))
                .expectError(InvalidCredentialsException.class)
                .verify();
    }

    @Test
    @DisplayName("Should return an error when token generation fails")
    void loginUserTokenGenerationFails() {
        String email = "test@email.com";
        String password = "password";
        User user = User.builder().email(email).password("encodedPassword").build();

        when(userRepository.getByEmail(email)).thenReturn(Mono.just(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(Mono.just(true));
        when(tokenProvider.generateToken(user)).thenReturn(Mono.error(new RuntimeException("Token error")));

        StepVerifier.create(loginUserUseCase.loginUser(email, password))
                .expectErrorSatisfies(throwable -> {
                    assert throwable instanceof RuntimeException;
                    assert throwable.getMessage().equals("Token error");
                })
                .verify();
    }
}