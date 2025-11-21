package pe.ask.library.port.in.usecase.user;

import pe.ask.library.model.user.User;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface IRegisterUserUseCase {
    Mono<User> registerUser(User user);
}
