package pe.ask.library.api.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.handler.LoginUserHandler;
import pe.ask.library.api.handler.RegisterUserHandler;
import pe.ask.library.api.utils.routes.UserRoutes;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserRouterRest {
    @Bean
    public RouterFunction<ServerResponse> userRouterFunction(RegisterUserHandler registerUserHandler, LoginUserHandler loginUserHandler) {
        return route(POST(UserRoutes.REGISTER).and(accept(MediaType.APPLICATION_JSON)), registerUserHandler::listenPOSTRegisterUserUseCase)
                .andRoute(POST(UserRoutes.LOGIN).and(accept(MediaType.APPLICATION_JSON)), loginUserHandler::listenPOSTLoginUserUseCase);
    }
}
