package pe.ask.library.model.token;

import java.time.LocalDateTime;

public class Token {

    private String token;
    private String type;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;

    public Token() { }

    public Token(
            String token,
            String type,
            LocalDateTime expiresAt,
            LocalDateTime createdAt
    ) {
        this.token = token;
        this.type = type;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }

    public static TokenBuilder builder() {
        return new TokenBuilder();
    }

    public TokenBuilder toBuilder() {
        return new TokenBuilder()
                .token(this.token)
                .type(this.type)
                .expiresAt(this.expiresAt)
                .createdAt(this.createdAt);
    }
}
