package pe.ask.library.router;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pe.ask.library.config.LoadBalancerConfig;

@Configuration
@LoadBalancerClient(name = "loan-service", configuration = LoadBalancerConfig.class)
public class LoanRouter {
    @Bean
    public RouteLocator loanRouterLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("loan-service", r -> r.path("/api/v1/loans/**")
                        .uri("lb://loan-service"))
                .build();
    }
}
