package pe.ask.library.port.in.usecase.user;

import pe.ask.library.model.token.Token;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ILoginUserUseCase {
    Mono<Token> loginUser(String email, String password);
}
