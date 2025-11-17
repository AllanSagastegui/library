package pe.ask.library.port.out.security;

import reactor.core.publisher.Mono;

public interface IPasswordEncoder {
    Mono<String> encode(String password);
    Mono<Boolean> matches(String rawPassword, String hashedPassword);
}
