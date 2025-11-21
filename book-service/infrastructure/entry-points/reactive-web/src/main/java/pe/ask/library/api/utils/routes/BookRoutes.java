package pe.ask.library.api.utils.routes;

public class BookRoutes {
    public static final String GET_ALL_BOOKS_BY_AUTHOR_ID = "/api/v1/books/author/{authorId}";
    public static final String GET_ALL_BOOKS_BY_CATEGORY_ID = "/api/v1/books/category/{categoryId}";
    public static final String GET_ALL_BOOKS_BY_FORMAT = "/api/v1/books/format/{format}";
    public static final String GET_ALL_BOOKS_BY_GENDER = "/api/v1/books/gender/{gender}";
    public static final String GET_ALL_BOOKS_BY_LANGUAGE = "/api/v1/books/language/{language}";
    public static final String GET_ALL_BOOKS_BY_PUBLISHER_ID = "/api/v1/books/publisher/{publisherId}";
    public static final String GET_ALL_BOOKS_BY_TITLE = "/api/v1/books/title/{title}";
    public static final String GET_ALL_BOOKS = "/api/v1/books";
    public static final String GET_ALL_BOOKS_BY_ID = "/api/v1/books/{id}";
    public static final String SAVE_BOOK = "/api/v1/books";
    public static final String UPDATE_BOOK = "/api/v1/books/{id}";
}
