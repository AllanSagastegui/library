package pe.ask.library.security.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.ask.library.model.role.Role;
import pe.ask.library.model.user.User;
import pe.ask.library.model.token.Token;
import pe.ask.library.port.out.persistence.IRoleRepository;
import pe.ask.library.security.utils.RoleNotFoundException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtProviderTest {

    @Mock
    private IRoleRepository roleRepository;

    private JwtProvider jwtProvider;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        this.privateKey = kp.getPrivate();
        this.publicKey = kp.getPublic();

        jwtProvider = new JwtProvider(privateKey, roleRepository);
    }

    @Test
    @DisplayName("Should generate a valid signed JWT token when user and role exist")
    void generateTokenSuccess() {
        String userId = "user-123";
        String roleId = "role-admin";
        String roleName = "ADMIN";

        User user = User.builder()
                .id(userId)
                .roleId(roleId)
                .build();

        Role role = Role.builder()
                .id(roleId)
                .name(roleName)
                .build();

        when(roleRepository.getRoleById(roleId)).thenReturn(Mono.just(role));

        Mono<Token> tokenMono = jwtProvider.generateToken(user);

        StepVerifier.create(tokenMono)
                .assertNext(token -> {
                    assertNotNull(token.getToken());
                    assertEquals("Bearer", token.getType());
                    assertEquals(userId, token.getUserId());
                    assertNotNull(token.getExpiresAt());
                    assertNotNull(token.getCreatedAt());

                    Claims claims = Jwts.parser()
                            .verifyWith(publicKey)
                            .build()
                            .parseSignedClaims(token.getToken())
                            .getPayload();

                    assertEquals(userId, claims.getSubject());
                    assertEquals(roleName, claims.get("role"));
                    assertEquals("access", claims.get("type"));

                    assertTrue(claims.getExpiration().after(new Date()));
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return RoleNotFoundException when role does not exist")
    void generateTokenRoleNotFound() {
        User user = User.builder()
                .id("user-123")
                .roleId("unknown-role")
                .build();

        when(roleRepository.getRoleById("unknown-role")).thenReturn(Mono.empty());

        StepVerifier.create(jwtProvider.generateToken(user))
                .expectError(RoleNotFoundException.class)
                .verify();
    }
}
