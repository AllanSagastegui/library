package pe.ask.library.api.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.handler.publisher.GetAllPublishersHandler;
import pe.ask.library.api.handler.publisher.GetPublisherByCountryHandler;
import pe.ask.library.api.handler.publisher.GetPublisherByIdHandler;
import pe.ask.library.api.handler.publisher.GetPublisherByNameHandler;
import pe.ask.library.api.utils.routes.PublisherRoutes;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class PublisherRouterRest {

    @Bean
    public RouterFunction<ServerResponse> publisherRouterFunction(
            GetAllPublishersHandler getAllPublishersHandler,
            GetPublisherByCountryHandler getPublisherByCountryHandler,
            GetPublisherByIdHandler getPublisherByIdHandler,
            GetPublisherByNameHandler getPublisherByNameHandler
    ) {
        return route(GET(PublisherRoutes.GET_ALL_PUBLISHERS).and(accept(MediaType.APPLICATION_JSON)), getAllPublishersHandler::listenGETAllPublishersUseCase)
                .andRoute(GET(PublisherRoutes.GET_ALL_PUBLISHERS_BY_COUNTRY).and(accept(MediaType.APPLICATION_JSON)), getPublisherByCountryHandler::listenGETPublishersByCountryUseCase)
                .andRoute(GET(PublisherRoutes.GET_ALL_PUBLISHER_BY_ID).and(accept(MediaType.APPLICATION_JSON)), getPublisherByIdHandler::listenGETPublisherByIdUseCase)
                .andRoute(GET(PublisherRoutes.GET_ALL_PUBLISHERS_BY_NAME).and(accept(MediaType.APPLICATION_JSON)), getPublisherByNameHandler::listenGETPublisherByNameUseCase);
    }
}
