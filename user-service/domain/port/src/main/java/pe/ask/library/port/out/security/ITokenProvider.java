package pe.ask.library.port.out.security;

import pe.ask.library.model.token.Token;
import pe.ask.library.model.user.User;
import reactor.core.publisher.Mono;

public interface ITokenProvider {
    Mono<Token> generateToken(User user);
}
