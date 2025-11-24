package pe.ask.library.port.in.notification;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ISendWelcomeEmail {
    Mono<Void> sendWelcomeEmail(String to, String completeName);
}
