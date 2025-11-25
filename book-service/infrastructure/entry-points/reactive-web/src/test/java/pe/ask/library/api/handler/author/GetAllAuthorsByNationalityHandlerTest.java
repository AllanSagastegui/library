package pe.ask.library.api.handler.author;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import pe.ask.library.api.dto.response.AuthorResponse;
import pe.ask.library.api.utils.validator.CustomValidator;
import pe.ask.library.model.author.Author;
import pe.ask.library.model.utils.Pageable;
import pe.ask.library.port.in.usecase.author.IGetAllAuthorsByNationalityUseCase;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllAuthorsByNationalityHandlerTest {

    @Mock
    private IGetAllAuthorsByNationalityUseCase useCase;
    @Mock
    private ObjectMapper mapper;
    @Mock
    private CustomValidator validator;
    @Mock
    private ServerRequest serverRequest;

    private GetAllAuthorsByNationalityHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GetAllAuthorsByNationalityHandler(useCase, mapper, validator);
    }

    @Test
    @DisplayName("Should return paged authors by nationality")
    void listenGETAllAuthorsByNationalityUseCase() {
        String nationality = "Peru";
        int page = 0;
        int size = 10;

        when(serverRequest.queryParam("page")).thenReturn(Optional.of(String.valueOf(page)));
        when(serverRequest.queryParam("size")).thenReturn(Optional.of(String.valueOf(size)));
        when(serverRequest.pathVariable("nationality")).thenReturn(nationality);

        Author author = new Author();
        Pageable<Author> pageable = Pageable.<Author>builder()
                .content(List.of(author))
                .page(page)
                .size(size)
                .build();

        when(useCase.getAllAuthorsByNationality(page, size, nationality))
                .thenReturn(Mono.just(pageable));

        when(mapper.map(any(), any())).thenReturn(new AuthorResponse());

        StepVerifier.create(handler.listenGETAllAuthorsByNationalityUseCase(serverRequest))
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
                })
                .verifyComplete();
    }
}
