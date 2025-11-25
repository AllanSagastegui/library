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
import pe.ask.library.api.dto.request.AuthorRequest;
import pe.ask.library.api.dto.response.AuthorResponse;
import pe.ask.library.api.utils.validator.CustomValidator;
import pe.ask.library.model.author.Author;
import pe.ask.library.port.in.usecase.author.IUpdateAuthorUseCase;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateAuthorHandlerTest {

    @Mock
    private IUpdateAuthorUseCase useCase;
    @Mock
    private ObjectMapper mapper;
    @Mock
    private CustomValidator validator;
    @Mock
    private ServerRequest serverRequest;

    private UpdateAuthorHandler handler;

    @BeforeEach
    void setUp() {
        handler = new UpdateAuthorHandler(useCase, mapper, validator);
    }

    @Test
    @DisplayName("Should update author and return 200 OK")
    void listenPUTUpdateAuthorUseCase() {
        UUID id = UUID.randomUUID();
        AuthorRequest requestDto = new AuthorRequest();
        Author authorDomain = new Author();
        Author updatedAuthor = new Author();
        AuthorResponse responseDto = new AuthorResponse();

        when(serverRequest.pathVariable("id")).thenReturn(id.toString());

        when(serverRequest.bodyToMono(AuthorRequest.class)).thenReturn(Mono.just(requestDto));
        when(validator.validate(requestDto)).thenReturn(Mono.just(requestDto));
        when(mapper.map(requestDto, Author.class)).thenReturn(authorDomain);

        when(useCase.updateAuthor(id, authorDomain)).thenReturn(Mono.just(updatedAuthor));
        when(mapper.map(updatedAuthor, AuthorResponse.class)).thenReturn(responseDto);

        StepVerifier.create(handler.listenPUTUpdateAuthorUseCase(serverRequest))
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
                })
                .verifyComplete();
    }
}
