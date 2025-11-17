package pe.ask.library.model.token;

import java.time.LocalDateTime;

public class TokenBuilder {
    private String token;
    private String type;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
    private String userId;

    public TokenBuilder() { }

    public TokenBuilder token(String token) {
        this.token = token;
        return this;
    }

    public TokenBuilder type(String type) {
        this.type = type;
        return this;
    }

    public TokenBuilder expiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

    public TokenBuilder createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public TokenBuilder userId(String userId) {
        this.userId = userId;
        return this;
    }

    public Token build() {
        return new Token(token, type, expiresAt, createdAt, userId);
    }
}
