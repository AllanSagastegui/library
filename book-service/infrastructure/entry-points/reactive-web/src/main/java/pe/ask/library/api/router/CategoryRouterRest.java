package pe.ask.library.api.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.handler.category.GetAllCategoriesHandler;
import pe.ask.library.api.handler.category.GetCategoryByIdHandler;
import pe.ask.library.api.handler.category.GetCategoryByNameHandler;
import pe.ask.library.api.utils.routes.CategoryRoutes;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class CategoryRouterRest {

    @Bean
    public RouterFunction<ServerResponse> categoryRouterFunction(
            GetAllCategoriesHandler getAllCategoriesHandler,
            GetCategoryByIdHandler getCategoryByIdHandler,
            GetCategoryByNameHandler getCategoryByNameHandler
    ) {
        return route(GET(CategoryRoutes.GET_ALL_CATEGORIES).and(accept(MediaType.APPLICATION_JSON)), getAllCategoriesHandler::listenGETAllCategoriesUseCase)
                .andRoute(GET(CategoryRoutes.GET_ALL_CATEGORY_ID).and(accept(MediaType.APPLICATION_JSON)), getCategoryByIdHandler::listenGETCategoryByIdUseCase)
                .andRoute(GET(CategoryRoutes.GET_ALL_CATEGORY_NAME).and(accept(MediaType.APPLICATION_JSON)), getCategoryByNameHandler::listenGETCategoryByNameUseCase);
    }
}
