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
import pe.ask.library.port.in.usecase.author.IGetAuthorByIdUseCase;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAuthorByIdHandlerTest {

    @Mock
    private IGetAuthorByIdUseCase useCase;
    @Mock
    private ObjectMapper mapper;
    @Mock
    private CustomValidator validator;
    @Mock
    private ServerRequest serverRequest;

    private GetAuthorByIdHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GetAuthorByIdHandler(useCase, mapper, validator);
    }

    @Test
    @DisplayName("Should return author by ID")
    void listenGETAuthorByIdUseCase() {
        UUID id = UUID.randomUUID();
        Author author = new Author();
        AuthorResponse responseDto = new AuthorResponse();

        when(serverRequest.pathVariable("id")).thenReturn(id.toString());
        when(useCase.getAuthorById(id)).thenReturn(Mono.just(author));
        when(mapper.map(author, AuthorResponse.class)).thenReturn(responseDto);

        StepVerifier.create(handler.listenGETAuthorByIdUseCase(serverRequest))
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
                })
                .verifyComplete();
    }
}