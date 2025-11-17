package pe.ask.library.port.out.persistence;

import pe.ask.library.model.user.User;
import reactor.core.publisher.Mono;

public interface IUserRepository {
    Mono<User> registerUser(User user);
    Mono<User> getByEmail(String email);
}
