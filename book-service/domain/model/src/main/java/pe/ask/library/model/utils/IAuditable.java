package pe.ask.library.model.utils;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface IAuditable {
    Mono<String> getAuditUserId();
}