package pe.ask.library.security.adapter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordEncoderAdapterTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordEncoderAdapter adapter;

    @Test
    @DisplayName("Should return encoded password wrapped in Mono")
    void encodeSuccess() {
        String rawPassword = "password123";
        String encodedPassword = "encodedPassword123";

        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        StepVerifier.create(adapter.encode(rawPassword))
                .expectNext(encodedPassword)
                .verifyComplete();

        verify(passwordEncoder).encode(rawPassword);
    }

    @Test
    @DisplayName("Should propagate error if encoding fails")
    void encodeError() {
        String rawPassword = "password123";
        when(passwordEncoder.encode(rawPassword)).thenThrow(new RuntimeException("Encoding failed"));

        StepVerifier.create(adapter.encode(rawPassword))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    @DisplayName("Should return true Mono when passwords match")
    void matchesTrue() {
        String raw = "password";
        String encoded = "encoded";

        when(passwordEncoder.matches(raw, encoded)).thenReturn(true);

        StepVerifier.create(adapter.matches(raw, encoded))
                .expectNext(true)
                .verifyComplete();

        verify(passwordEncoder).matches(raw, encoded);
    }

    @Test
    @DisplayName("Should return false Mono when passwords do not match")
    void matchesFalse() {
        String raw = "wrong";
        String encoded = "encoded";

        when(passwordEncoder.matches(raw, encoded)).thenReturn(false);

        StepVerifier.create(adapter.matches(raw, encoded))
                .expectNext(false)
                .verifyComplete();
    }
}
