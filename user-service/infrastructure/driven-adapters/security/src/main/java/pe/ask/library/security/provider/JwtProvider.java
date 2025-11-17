package pe.ask.library.security.provider;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.ask.library.model.token.Token;
import pe.ask.library.model.user.User;
import pe.ask.library.port.out.persistence.IRoleRepository;
import pe.ask.library.security.utils.TokenExpirationTime;
import reactor.core.publisher.Mono;

import java.security.PrivateKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId; // Importante añadir ZoneId
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final PrivateKey privateKey;
    private final IRoleRepository roleRepository;

    public Mono<Token> generateToken(User user) {
        final Instant now = Instant.now();
        final Instant expiry = now.plusSeconds(TokenExpirationTime.ACCESS.getSeconds());

        return roleRepository.getRoleById(user.getRoleId())
                .switchIfEmpty(Mono.error(RuntimeException::new))
                .map(role -> Token.builder()
                        .token(Jwts.builder()
                                .subject(user.getId())
                                .issuedAt(Date.from(now))
                                .expiration(Date.from(expiry))
                                .claims(Map.of(
                                        "type", "access",
                                        "role", role.getName(),
                                        "userId", user.getId()
                                ))
                                .signWith(privateKey, Jwts.SIG.RS256)
                                .compact()
                        )
                        .type("Bearer")
                        .expiresAt(LocalDateTime.ofInstant(expiry, ZoneId.of("UTC")))
                        .createdAt(LocalDateTime.ofInstant(now, ZoneId.of("UTC")))
                        .userId(user.getId())
                        .build()
                );
    }
}