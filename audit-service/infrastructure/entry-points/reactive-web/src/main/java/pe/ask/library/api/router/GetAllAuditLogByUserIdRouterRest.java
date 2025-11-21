package pe.ask.library.api.router;

import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.docs.GetAllAuditLogByUserIdDoc;
import pe.ask.library.api.handler.GetAllAuditLogByUserIdHandler;
import pe.ask.library.api.utils.Routes;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class GetAllAuditLogByUserIdRouterRest {
    @Bean
    @RouterOperation(
            path = Routes.GET_ALL_AUDIT_LOGS_BY_USER_ID,
            method = RequestMethod.GET,
            beanClass = GetAllAuditLogByUserIdDoc.class,
            beanMethod = "getAllAuditLogsByUserIdDoc"
    )
    public RouterFunction<ServerResponse> getAllAuditLogByUserIdRouterRestFunction(
            GetAllAuditLogByUserIdHandler getAllAuditLogByUserIdHandler
    ) {
        return route(GET(Routes.GET_ALL_AUDIT_LOGS_BY_USER_ID), getAllAuditLogByUserIdHandler::listenGETAllAuditLogByUserIdUseCase);
    }
}
