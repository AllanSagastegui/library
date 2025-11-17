package pe.ask.library.model.token;

import java.time.LocalDateTime;

public class TokenBuilder {

    private String id;
    private String userId;
    private String token;
    private String type;
    private boolean revoked;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;

    public TokenBuilder() { }

    public TokenBuilder id(String id) {
        this.id = id;
        return this;
    }

    public TokenBuilder userId(String userId) {
        this.userId = userId;
        return this;
    }

    public TokenBuilder token(String token) {
        this.token = token;
        return this;
    }

    public TokenBuilder type(String type) {
        this.type = type;
        return this;
    }

    public TokenBuilder revoked(boolean revoked) {
        this.revoked = revoked;
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

    public Token build() {
        return new Token(id, userId, token, type, revoked, expiresAt, createdAt);
    }
}
