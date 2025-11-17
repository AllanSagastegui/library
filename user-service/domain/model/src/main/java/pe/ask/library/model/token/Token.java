package pe.ask.library.model.token;

import java.time.LocalDateTime;

public class Token {

    private String id;
    private String userId;
    private String token;
    private String type;
    private boolean revoked;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;

    public Token() { }

    public Token(
            String id,
            String userId,
            String token,
            String type,
            boolean revoked,
            LocalDateTime expiresAt,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.userId = userId;
        this.token = token;
        this.type = type;
        this.revoked = revoked;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }

    public static TokenBuilder builder() {
        return new TokenBuilder();
    }

    public TokenBuilder toBuilder() {
        return new TokenBuilder()
                .id(this.id)
                .userId(this.userId)
                .token(this.token)
                .type(this.type)
                .revoked(this.revoked)
                .expiresAt(this.expiresAt)
                .createdAt(this.createdAt);
    }


}