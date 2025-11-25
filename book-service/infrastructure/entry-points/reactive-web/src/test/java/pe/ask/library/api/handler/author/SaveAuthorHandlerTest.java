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
import org.springframework.web.util.UriBuilder;
import pe.ask.library.api.dto.request.AuthorRequest;
import pe.ask.library.api.dto.response.AuthorResponse;
import pe.ask.library.api.utils.validator.CustomValidator;
import pe.ask.library.model.author.Author;
import pe.ask.library.port.in.usecase.author.ISaveAuthorUseCase;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaveAuthorHandlerTest {

    @Mock
    private ISaveAuthorUseCase useCase;
    @Mock
    private ObjectMapper mapper;
    @Mock
    private CustomValidator validator;
    @Mock
    private ServerRequest serverRequest;

    private SaveAuthorHandler handler;

    @BeforeEach
    void setUp() {
        handler = new SaveAuthorHandler(useCase, mapper, validator);
    }

    @Test
    @DisplayName("Should save author and return 201 Created")
    void listenPOSTSaveAuthorUseCase() {
        AuthorRequest requestDto = new AuthorRequest();
        Author authorDomain = new Author();
        Author savedAuthor = new Author();
        savedAuthor.setId(UUID.randomUUID());

        AuthorResponse responseDto = new AuthorResponse();
        URI createdUri = URI.create("/author/" + savedAuthor.getId());

        when(serverRequest.bodyToMono(AuthorRequest.class)).thenReturn(Mono.just(requestDto));
        when(validator.validate(requestDto)).thenReturn(Mono.just(requestDto));
        when(mapper.map(requestDto, Author.class)).thenReturn(authorDomain);

        when(useCase.saveAuthor(authorDomain)).thenReturn(Mono.just(savedAuthor));
        when(mapper.map(savedAuthor, AuthorResponse.class)).thenReturn(responseDto);

        UriBuilder uriBuilder = mock(UriBuilder.class);
        when(serverRequest.uriBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.path(anyString())).thenReturn(uriBuilder);
        when(uriBuilder.build(any(Object[].class))).thenReturn(createdUri);

        StepVerifier.create(handler.listenPOSTSaveAuthorUseCase(serverRequest))
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED);
                    assertThat(response.headers().getLocation()).isEqualTo(createdUri);
                })
                .verifyComplete();
    }
}
