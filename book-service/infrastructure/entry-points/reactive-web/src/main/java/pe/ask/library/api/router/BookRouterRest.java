package pe.ask.library.api.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.handler.book.*;
import pe.ask.library.api.utils.routes.BookRoutes;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BookRouterRest {

    @Bean
    public RouterFunction<ServerResponse> bookRouterFunction(
            GetAllBooksByAuthorIdHandler getAllBooksByAuthorIdHandler,
            GetAllBooksByCategoryIdHandler getAllBooksByCategoryIdHandler,
            GetAllBooksByFormatHandler getAllBooksByFormatHandler,
            GetAllBooksByGenderHandler getAllBooksByGenderHandler,
            GetAllBooksByLanguageHandler getAllBooksByLanguageHandler,
            GetAllBooksByPublisherIdHandler getAllBooksByPublisherIdHandler,
            GetAllBooksByTitleHandler getAllBooksByTitleHandler,
            GetAllBooksHandler getAllBooksHandler,
            GetBookByIdHandler getBookByIdHandler,
            SaveBookHandler saveBookHandler,
            UpdateBookHandler updateBookHandler
    ) {
        return route(GET(BookRoutes.GET_ALL_BOOKS_BY_AUTHOR_ID).and(accept(MediaType.APPLICATION_JSON)), getAllBooksByAuthorIdHandler::listenGETAllBooksByAuthorIdUseCase)
                .andRoute(GET(BookRoutes.GET_ALL_BOOKS_BY_CATEGORY_ID).and(accept(MediaType.APPLICATION_JSON)), getAllBooksByCategoryIdHandler::listenGETAllBooksByCategoryIdUseCase)
                .andRoute(GET(BookRoutes.GET_ALL_BOOKS_BY_FORMAT).and(accept(MediaType.APPLICATION_JSON)), getAllBooksByFormatHandler::listenGETAllBooksByFormatUseCase)
                .andRoute(GET(BookRoutes.GET_ALL_BOOKS_BY_GENDER).and(accept(MediaType.APPLICATION_JSON)), getAllBooksByGenderHandler::listenGETAllBooksByGenderUseCase)
                .andRoute(GET(BookRoutes.GET_ALL_BOOKS_BY_LANGUAGE).and(accept(MediaType.APPLICATION_JSON)), getAllBooksByLanguageHandler::listenGETAllBooksByLanguageUseCase)
                .andRoute(GET(BookRoutes.GET_ALL_BOOKS_BY_PUBLISHER_ID).and(accept(MediaType.APPLICATION_JSON)), getAllBooksByPublisherIdHandler::listenGETAllBooksByPublisherIdUseCase)
                .andRoute(GET(BookRoutes.GET_ALL_BOOKS_BY_TITLE).and(accept(MediaType.APPLICATION_JSON)), getAllBooksByTitleHandler::listenGETAllBooksByTitleUseCase)
                .andRoute(GET(BookRoutes.GET_ALL_BOOKS).and(accept(MediaType.APPLICATION_JSON)), getAllBooksHandler::listenGETAllBooksUseCase)
                .andRoute(GET(BookRoutes.GET_ALL_BOOKS_BY_ID).and(accept(MediaType.APPLICATION_JSON)), getBookByIdHandler::listenGETBookByIdUseCase)
                .andRoute(POST(BookRoutes.SAVE_BOOK).and(accept(MediaType.APPLICATION_JSON)), saveBookHandler::listenPOSTSaveBookUseCase)
                .andRoute(PUT(BookRoutes.UPDATE_BOOK).and(accept(MediaType.APPLICATION_JSON)), updateBookHandler::listenPUTUpdateBookUseCase);
    }
}
