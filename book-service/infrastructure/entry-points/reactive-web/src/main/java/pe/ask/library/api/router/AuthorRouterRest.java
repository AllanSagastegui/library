package pe.ask.library.api.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.handler.author.*;
import pe.ask.library.api.utils.routes.AuthorRoutes;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AuthorRouterRest {

    @Bean
    public RouterFunction<ServerResponse> authorRouterFunction(
            GetAllAuthorsByNationalityHandler getAllAuthorsByNationalityHandler,
            GetAllAuthorsHandler getAllAuthorsHandler,
            GetAuthorByIdHandler getAuthorByIdHandler,
            GetAuthorByPseudonymHandler getAuthorByPseudonymHandler,
            SaveAuthorHandler saveAuthorHandler,
            UpdateAuthorHandler updateAuthorHandler
    ) {
        return route(GET(AuthorRoutes.GET_ALL_AUTHORS_BY_NATIONALITY).and(accept(MediaType.APPLICATION_JSON)), getAllAuthorsByNationalityHandler::listenGETAllAuthorsByNationalityUseCase)
                .andRoute(GET(AuthorRoutes.GET_ALL_AUTHORS).and(accept(MediaType.APPLICATION_JSON)), getAllAuthorsHandler::listenGETAllAuthorsUseCase)
                .andRoute(GET(AuthorRoutes.GET_AUTHOR_BY_ID).and(accept(MediaType.APPLICATION_JSON)), getAuthorByIdHandler::listenGETAuthorByIdUseCase)
                .andRoute(GET(AuthorRoutes.GET_ALL_AUTHORS_BY_PSEUDONYM).and(accept(MediaType.APPLICATION_JSON)), getAuthorByPseudonymHandler::listenGETAuthorByPseudonymUseCase)
                .andRoute(POST(AuthorRoutes.SAVE_AUTHOR).and(accept(MediaType.APPLICATION_JSON)), saveAuthorHandler::listenPOSTSaveAuthorUseCase)
                .andRoute(PUT(AuthorRoutes.UPDATE_AUTHOR).and(accept(MediaType.APPLICATION_JSON)), updateAuthorHandler::listenPUTUpdateAuthorUseCase);
    }

}
