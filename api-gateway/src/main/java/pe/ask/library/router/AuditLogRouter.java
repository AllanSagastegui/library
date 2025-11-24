package pe.ask.library.router;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pe.ask.library.config.LoadBalancerConfig;

@Configuration
@LoadBalancerClient(name = "audit-service", configuration = LoadBalancerConfig.class)
public class AuditLogRouter {

    @Bean
    public RouteLocator auditLogRouterLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("audit-service", r -> r.path("/api/v1/audit-log/**")
                        .uri("lb://audit-service"))
                .build();
    }
}
