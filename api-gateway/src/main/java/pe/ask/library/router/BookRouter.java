package pe.ask.library.router;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pe.ask.library.config.LoadBalancerConfig;

@Configuration
@LoadBalancerClient(name = "book-service", configuration = LoadBalancerConfig.class)
public class BookRouter {

    @Bean
    public RouteLocator bookRouterLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("book-service", r -> r.path("/api/v1/books/**")
                        .uri("lb://book-service"))
                .build();
    }
}