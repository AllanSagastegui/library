package pe.ask.library.model.token;

import pe.ask.library.model.utils.IAuditable;

import java.time.LocalDateTime;

public class Token implements IAuditable {

    private String token;
    private String type;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
    private String userId;

    public Token() { }

    public Token(
            String token,
            String type,
            LocalDateTime expiresAt,
            LocalDateTime createdAt,
            String userId
    ) {
        this.token = token;
        this.type = type;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    public static TokenBuilder builder() {
        return new TokenBuilder();
    }

    public TokenBuilder toBuilder() {
        return new TokenBuilder()
                .token(this.token)
                .type(this.type)
                .expiresAt(this.expiresAt)
                .createdAt(this.createdAt)
                .userId(this.userId);
    }

    @Override
    public String getAuditUserId() {
        return this.userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
