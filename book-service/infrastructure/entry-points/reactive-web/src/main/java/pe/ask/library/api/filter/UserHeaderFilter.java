package pe.ask.library.api.filter;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

@Component
public class UserHeaderFilter implements WebFilter {

    public static final String USER_ID_KEY = "USER_ID";

    @NonNull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String userId = exchange.getRequest().getHeaders().getFirst("user-id");

        String finalUserId = (userId != null) ? userId : "anonymous";

        return chain.filter(exchange)
                .contextWrite(Context.of(USER_ID_KEY, finalUserId));
    }
}