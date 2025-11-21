package pe.ask.library.api.utils.routes;

public class AuthorRoutes {
    public static final String GET_ALL_AUTHORS_BY_NATIONALITY = "/api/v1/authors/nationality/{nationality}";
    public static final String GET_ALL_AUTHORS = "/api/v1/authors";
    public static final String GET_AUTHOR_BY_ID = "/api/v1/authors/{id}";
    public static final String GET_ALL_AUTHORS_BY_PSEUDONYM = "/api/v1/authors/pseudonym/{pseudonym}";
    public static final String SAVE_AUTHOR = "/api/v1/authors";
    public static final String UPDATE_AUTHOR = "/api/v1/authors/{id}";
}
