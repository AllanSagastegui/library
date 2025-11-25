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
import pe.ask.library.port.in.usecase.author.IGetAuthorByPseudonymUseCase;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAuthorByPseudonymHandlerTest {

    @Mock
    private IGetAuthorByPseudonymUseCase useCase;
    @Mock
    private ObjectMapper mapper;
    @Mock
    private CustomValidator validator;
    @Mock
    private ServerRequest serverRequest;

    private GetAuthorByPseudonymHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GetAuthorByPseudonymHandler(useCase, mapper, validator);
    }

    @Test
    @DisplayName("Should return author by pseudonym")
    void listenGETAuthorByPseudonymUseCase() {
        String pseudonym = "TheWriter";
        Author author = new Author();
        AuthorResponse responseDto = new AuthorResponse();

        when(serverRequest.pathVariable("pseudonym")).thenReturn(pseudonym);
        when(useCase.getAuthorByPseudonym(pseudonym)).thenReturn(Mono.just(author));
        when(mapper.map(author, AuthorResponse.class)).thenReturn(responseDto);

        StepVerifier.create(handler.listenGETAuthorByPseudonymUseCase(serverRequest))
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
                })
                .verifyComplete();
    }
}
