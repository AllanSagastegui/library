package pe.ask.library.api.filter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

class UserHeaderFilterTest {

    private final UserHeaderFilter userHeaderFilter = new UserHeaderFilter();

    @Test
    @DisplayName("Should add userId to context when X-User-Id header is present")
    void filterWithHeader() {
        String userId = "test-user-123";
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/")
                        .header(UserHeaderFilter.USER_ID_HEADER, userId)
                        .build()
        );

        WebFilterChain chain = ex -> Mono.deferContextual(ctx -> {
            assertThat(ctx.hasKey(UserHeaderFilter.USER_ID_KEY))
                    .withFailMessage("Context key %s should exist", UserHeaderFilter.USER_ID_KEY)
                    .isTrue();

            assertThat((String) ctx.get(UserHeaderFilter.USER_ID_KEY))
                    .isEqualTo(userId);

            return Mono.empty();
        });

        StepVerifier.create(userHeaderFilter.filter(exchange, chain))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should NOT add userId to context when X-User-Id header is missing")
    void filterWithoutHeader() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/").build()
        );

        WebFilterChain chain = ex -> Mono.deferContextual(ctx -> {
            assertThat(ctx.hasKey(UserHeaderFilter.USER_ID_KEY))
                    .withFailMessage("Context key %s should NOT exist", UserHeaderFilter.USER_ID_KEY)
                    .isFalse();
            return Mono.empty();
        });

        StepVerifier.create(userHeaderFilter.filter(exchange, chain))
                .verifyComplete();
    }
}