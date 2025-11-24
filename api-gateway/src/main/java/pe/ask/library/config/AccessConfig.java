package pe.ask.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import pe.ask.library.exception.AccessDeniedException;
import pe.ask.library.exception.UnauthorizedException;
import reactor.core.publisher.Mono;

@Configuration
public class AccessConfig {

    @Bean
    public ServerAccessDeniedHandler accessDeniedHandler() {
        return (exchange, denied) -> Mono.error(new AccessDeniedException());
    }

    @Bean
    public ServerAuthenticationEntryPoint authenticationEntryPoint() {
        return (exchange, ex) -> Mono.error(new UnauthorizedException());
    }
}