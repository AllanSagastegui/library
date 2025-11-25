package pe.ask.library.security.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class EncoderConfigTest {

    @Test
    @DisplayName("Should create a DelegatingPasswordEncoder bean")
    void passwordEncoderBean() {
        EncoderConfig config = new EncoderConfig();
        PasswordEncoder encoder = config.passwordEncoder();

        assertNotNull(encoder);
        assertInstanceOf(DelegatingPasswordEncoder.class, encoder);
    }
}
