package pe.ask.library.security.adapter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.ask.library.model.token.Token;
import pe.ask.library.model.user.User;
import pe.ask.library.security.provider.JwtProvider;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenProviderAdapterTest {

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private TokenProviderAdapter adapter;

    @Test
    @DisplayName("Should generate token successfully via JwtProvider")
    void generateTokenSuccess() {
        User user = User.builder().email("test@test.com").build();
        Token token = Token.builder().token("jwt-token-xyz").build();

        when(jwtProvider.generateToken(user)).thenReturn(Mono.just(token));

        StepVerifier.create(adapter.generateToken(user))
                .expectNext(token)
                .verifyComplete();

        verify(jwtProvider).generateToken(user);
    }

    @Test
    @DisplayName("Should propagate error when token generation fails")
    void generateTokenError() {
        User user = User.builder().email("test@test.com").build();
        RuntimeException exception = new RuntimeException("Generation failed");

        when(jwtProvider.generateToken(user)).thenReturn(Mono.error(exception));

        StepVerifier.create(adapter.generateToken(user))
                .expectError(RuntimeException.class)
                .verify();

        verify(jwtProvider).generateToken(user);
    }
}