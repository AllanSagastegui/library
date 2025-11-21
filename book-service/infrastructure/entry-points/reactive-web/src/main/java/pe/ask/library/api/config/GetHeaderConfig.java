package pe.ask.library.api.config;

import org.springframework.stereotype.Component;
import pe.ask.library.api.filter.UserHeaderFilter;
import pe.ask.library.model.utils.IAuditable;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class GetHeaderConfig implements IAuditable {
    @Override
    public Mono<String> getAuditUserId() {
        return Mono.deferContextual(ctx ->
                Mono.just(Objects.requireNonNull(ctx.getOrDefault(UserHeaderFilter.USER_ID_KEY, "anonymous")))
        );
    }
}
