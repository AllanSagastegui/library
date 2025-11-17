package pe.ask.library.security.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.ask.library.model.token.Token;
import pe.ask.library.model.user.User;
import pe.ask.library.port.out.security.ITokenProvider;
import pe.ask.library.security.provider.JwtProvider;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TokenProviderAdapter implements ITokenProvider {

    private final JwtProvider jwtProvider;

    @Override
    public Mono<Token> generateToken(User user) {
        return jwtProvider.generateToken(user);
    }
}
