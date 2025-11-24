package pe.ask.library.router;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pe.ask.library.config.LoadBalancerConfig;

@Configuration
@LoadBalancerClient(name = "user-service", configuration = LoadBalancerConfig.class)
public class UserRouter {
    @Bean
    public RouteLocator userRouterLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/api/v1/user/**")
                        .uri("lb://user-service"))
                .build();
    }
}
