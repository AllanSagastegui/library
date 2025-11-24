package pe.ask.library.api.filter;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class UserHeaderFilter implements WebFilter {

    public static final String USER_ID_KEY = "userId";
    public static final String USER_ID_HEADER = "X-User-Id";

    @NonNull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String userId = exchange.getRequest().getHeaders().getFirst(USER_ID_HEADER);
        return chain.filter(exchange)
                .contextWrite(ctx -> userId != null ? ctx.put(USER_ID_KEY, userId) : ctx);
    }
}